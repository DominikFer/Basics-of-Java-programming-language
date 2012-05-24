package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class SmartHttpServer {

	private String address;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SessionMapEntry>();
	private Random sessionRandom = new Random();
	
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	private Map<String, IWebWorker> workersMap = new HashMap<String, IWebWorker>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	
	public static void main(String[] args) throws IOException {
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
	}
	
	public SmartHttpServer(String configFileName) {
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName), StandardOpenOption.READ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		address = properties.getProperty("server.address");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		
		Path mimePath = Paths.get(properties.getProperty("server.mimeConfig"));
		Properties mimeProperties = new Properties();
		try {
			mimeProperties.load(Files.newInputStream(mimePath, StandardOpenOption.READ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Entry<Object, Object> line : mimeProperties.entrySet()) {
			mimeTypes.put(line.getKey().toString(), line.getValue().toString());
		}
		
		Path workersPath = Paths.get(properties.getProperty("server.workers"));
		parseWorkers(workersPath);
	}
	
	private void parseWorkers(Path workersPath) {
		List<String> workers = new ArrayList<String>();
		
		try {
			Scanner inputScanner = new Scanner(new FileInputStream(workersPath.toFile()), "UTF-8");
			while(inputScanner.hasNextLine()) {
				String worker = inputScanner.nextLine();
				if(workers.contains(worker))
					throw new IllegalArgumentException("workers.properties cannot contain duplicate lines.");
				
				workers.add(worker);
			}
			inputScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(String worker : workers) {
			String[] workerArray = worker.split(" = ");
			String path = workerArray[0];
			String fqcn = workerArray[1];
			
			try {
				Class<?>  referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				
				System.out.println("Worker: " + path + ":" + fqcn);
				workersMap.put(path, iww);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	protected synchronized void start() {
		if(serverThread == null)
			serverThread = new ServerThread();
		
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		serverThread.start();
	}
	
	protected synchronized void stop() {
		serverThread.stopThread();
		threadPool.shutdown();
	}
	
	protected class ServerThread extends Thread {
		
		private boolean run = true;
		
		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				while(run) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public void stopThread() {
			this.run = false;
		}
	}
	
	private class ClientWorker implements Runnable {
		
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;
		
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}
		
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<String> request = readRequest(istream);
			
			String firstLine = request.get(0);
			String[] firstLineSplitted = firstLine.split(" ");
			method = firstLineSplitted[0];
			String requestedPathString = firstLineSplitted[1];
			version = firstLineSplitted[2];
			
			if(!method.equals("GET") || !(version.equals("HTTP/1.0") || version.equals("HTTP/1.1"))) {
				sendStatusCode(400);
				closeSocket();
				return;
			}
			
			checkSession(request);

			String path = null;
			if(requestedPathString.contains("?")) {
				String[] requestedPathSplitted = requestedPathString.split("\\?");
				path = requestedPathSplitted[0];
				parseParameters(requestedPathSplitted[1]);
			} else {
				path = requestedPathString;
			}
			
			Path requestedPath = Paths.get(documentRoot + path);
			File requestedFile = requestedPath.toFile();
			
			boolean exist = requestedFile.exists();
			boolean isFile = requestedFile.isFile();
			boolean canRead = requestedFile.canRead();
			
			RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);
			
			if(path.startsWith("/ext/")) {
				try {
					String workersPackage = "hr.fer.zemris.java.webserver.workers.";
					String className = path.replace("/ext/", "");
					
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + className);
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker) newObject;
					iww.processRequest(rc);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				closeSocket();
				return;
			}
			
			if(workersMap.containsKey(path)) {
				workersMap.get(path).processRequest(rc);
				
				closeSocket();
				return;
			}
			
			if (!requestedPath.startsWith(documentRoot)) {
				sendStatusCode(400);
				closeSocket();
				return;
			} else if(!exist || !isFile || !canRead) {
				sendStatusCode(404);
				closeSocket();
				return;
			}
			
			String name = requestedFile.getName();
	        int extensionDot = name.lastIndexOf(".");
	        String extension = null;
	        if(extensionDot != -1) {
	        	extension = name.substring(extensionDot + 1, name.length());	        	
	        } else {
	        	sendStatusCode(404);
				closeSocket();
				return;
	        }

	        if(extension.equals("smscr")) {
	        	String documentBody = "";
	        	
				try {
					Scanner inputScanner = new Scanner(new FileInputStream(requestedFile), "UTF-8");
					while(inputScanner.hasNextLine()) {
						documentBody += inputScanner.nextLine();
					}
					inputScanner.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				try {
					SmartScriptParser parser = new SmartScriptParser(documentBody);
					SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(), rc);
					engine.execute();
				} catch (SmartScriptParserException e) {
					e.printStackTrace();
				}
				
				closeSocket();
				return;
	        }
	        
	        String mimeType = "application/octet-stream";
	        if(mimeTypes.containsKey(extension)) {
	        	mimeType = mimeTypes.get(extension);
	        }
	        rc.setMimeType(mimeType);
	        
			try {
				BufferedInputStream fileOutput = new BufferedInputStream(new FileInputStream(requestedPath.toFile()));
				int length = 0;
				do {
					byte[] output = new byte[1024];
					length = fileOutput.read(output);
					if(length == -1) break;
					
					rc.write(output, 0, length);
				} while (length != -1);
				fileOutput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
			closeSocket();
		}

		private void checkSession(List<String> request) {
			SessionMapEntry userEntry = null;
			String sidCandiate = null;
			
			for(String headerLine : request) {
				if(headerLine.startsWith("Cookie:")) {
					String[] cookies = headerLine.replace("Cookie:", "").split(";");
					for(int i = 0; i < cookies.length; i++) {
						String[] cookie = cookies[i].split("=");
						
						String cookieName = cookie[0];
						String cookieValue = cookie[1].replace("\"", "");
						
						if(cookieName.trim().equals("sid")) {
							sidCandiate = cookieValue;
						}
					}
				}
			}
			
			if(sidCandiate == null) {
				userEntry = createNewSidAndCookie();
				System.out.println("Cookie is not found, generating... => " + userEntry.getSid());
			} else {
				int now = (int) (new Date().getTime()/1000);
				
				SessionMapEntry entry = sessions.get(sidCandiate);
				if(entry == null || now > entry.getValidUntil()) {
					userEntry = createNewSidAndCookie();
					System.out.println("Cookie is outdated, generating... => " + userEntry.getSid());
				} else {
					System.out.println("Cookie is valid => " + entry.getSid());
					entry.setValidUntil(now + sessionTimeout);
					userEntry = entry;
				}
			}
			
			permPrams = userEntry.getMap();
		}
		
		private SessionMapEntry createNewSidAndCookie() {
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

			String newSid = "";
			for(int i = 0; i < 20; i++) {
				newSid += alphabet.charAt(sessionRandom.nextInt(alphabet.length()));
			}
			
			int validUntil = (int) (new Date().getTime()/1000) + sessionTimeout;
			
			SessionMapEntry entry = new SessionMapEntry(newSid, validUntil);
			sessions.put(newSid, entry);
			
			outputCookies.add(new RCCookie("sid", newSid, validUntil, address, "/"));
			
			return entry;
		}

		private void closeSocket() {
			try {
				csocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendStatusCode(int statusCode) {
			try {
				ostream.write(getStatusMessage(statusCode).getBytes(StandardCharsets.UTF_8));
				ostream.write((byte) '\n');
				ostream.write((byte) '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private String getStatusMessage(int statusCode) {
			switch(statusCode) {
				case 400: return "HTTP/1.1 400 Bad Request";
				case 404: return "HTTP/1.1 404 File Not Found";
			}
			
			return null;
		}
		
		private void parseParameters(String paramString) {
			String[] paramsArray = paramString.split("&");
			for(int i = 0; i < paramsArray.length; i++) {
				String[] parameter = paramsArray[i].split("=");
				params.put(parameter[0], parameter[1]);
				System.out.println("   " + paramsArray[i]);
			}
		}

		private List<String> readRequest(PushbackInputStream inputStream) {
			List<String> lines = new ArrayList<String>();
			
			Scanner inputScanner = new Scanner(inputStream, "UTF-8");
			while(inputScanner.hasNextLine()) {
				String line = inputScanner.nextLine();
				if(line.isEmpty())
					break;
				lines.add(line);
				System.out.println(line);
			}
			
			return lines;
		}
	}
	
	private static class SessionMapEntry {
		
		String sid;
		long validUntil;
		Map<String, String> map;
		
		public SessionMapEntry(String sid, int validUntil) {
			this.sid = sid;
			this.validUntil = validUntil;
			
			map = new ConcurrentHashMap<String, String>();
		}
		
		public void setValidUntil(long validUntil) {
			this.validUntil = validUntil;
		}
		
		public String getSid() {
			return sid;
		}
		
		public long getValidUntil() {
			return validUntil;
		}
		
		public Map<String, String> getMap() {
			return map;
		}
	}
}
