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
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *	HTTP Server which provides support for cookies,
 *	URL parameters, sessions id's, SmartScript document executing
 *	and different mime-types support.
 *
 * @author Sven Kapuđija
 */
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
	
	/**
	 * Main method. Runs the HTTP server.
	 * 
	 * @param args			
	 * @throws IOException	
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 1)
			throw new IllegalArgumentException("Please provide single parameter - server.properties file.");
		
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param configFileName	server.properties configuration file name (path).
	 */
	public SmartHttpServer(String configFileName) {
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName), StandardOpenOption.READ));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load configuration file '" + configFileName + "'.");
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
			throw new IllegalArgumentException("Could not load mimeType file '" + mimePath + "'.");
		}
		
		for(Entry<Object, Object> line : mimeProperties.entrySet()) {
			mimeTypes.put(line.getKey().toString(), line.getValue().toString());
		}
		
		Path workersPath = Paths.get(properties.getProperty("server.workers"));
		parseWorkers(workersPath);
	}
	
	/**
	 * Parse the workers.properties files and initialize workers classes.
	 * 
	 * @param workersPath	worker.properties file path.
	 */
	private void parseWorkers(Path workersPath) {
		List<String> workers = new ArrayList<String>();
		
		try {
			Scanner inputScanner = new Scanner(new FileInputStream(workersPath.toFile()), "UTF-8");
			while(inputScanner.hasNextLine()) {
				String worker = inputScanner.nextLine();
				if(workers.contains(worker))
					throw new IllegalArgumentException("'" + workersPath + "' cannot contain duplicate lines.");
				
				workers.add(worker);
			}
			inputScanner.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("'" + workersPath + "' file is not found.");
		}
		
		for(String worker : workers) {
			String[] workerArray = worker.split(" = ");
			String path = workerArray[0];
			String fqcn = workerArray[1];
			
			try {
				Class<?>  referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				
				workersMap.put(path, iww);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Class '" + fqcn + "' is not found.");
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("Could not instantiate '" + fqcn + "' class.");
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Illegal access to '" + fqcn + "' class.");
			}
		}
	}

	/**
	 * Start the HTTP server.
	 */
	protected synchronized void start() {
		if(serverThread == null)
			serverThread = new ServerThread();
		
		threadPool = Executors.newFixedThreadPool(workerThreads);
		serverThread.start();
	}
	
	/**
	 * Stops the HTTP server.
	 */
	protected synchronized void stop() {
		serverThread.stopThread();
		threadPool.shutdown();
	}
	
	/**
	 *	Class which describes single server thread ready
	 *	to rumble.
	 */
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
				serverSocket.close();
			} catch (IOException e) {
				throw new RuntimeException("Socket on port '" + port + "' could not be opened.");
			}
			
		}
		
		/**
		 * Stop the server thread.
		 */
		public void stopThread() {
			this.run = false;
		}
	}
	
	/**
	 *	Class which handles and executes single client request.
	 */
	private class ClientWorker implements Runnable {
		
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		/**
		 * Constructor.
		 * 
		 * @param csocket	Client socket.
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}
		
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				closeSocket();
				throw new RuntimeException("Could not open input/output socket stream.");
			}
			
			List<String> request = readRequest(istream);
			
			String firstLine = request.get(0);
			System.out.println("Request: " + firstLine);
			
			String[] firstLineSplitted = firstLine.split(" ");
			method = firstLineSplitted[0];
			String requestedPathString = firstLineSplitted[1];
			version = firstLineSplitted[2];
			
			// Allow only GET requests
			if(!method.equals("GET") || !(version.equals("HTTP/1.0") || version.equals("HTTP/1.1"))) {
				sendStatusCode(400);
				closeSocket();
				return;
			}
			
			// Check for client session id via client's cookie data
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
			
			// Look for IWebWorkers - if recognized, run it
			if(path.startsWith("/ext/")) {
				String workersPackage = "hr.fer.zemris.java.webserver.workers.";
				String fqcn = path.replace("/ext/", "");
				
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + fqcn);
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker) newObject;
					iww.processRequest(rc);
				} catch (ClassNotFoundException e) {
					closeSocket();
					throw new IllegalArgumentException("Class '" + fqcn + "' is not found.");
				} catch (InstantiationException e) {
					closeSocket();
					throw new IllegalArgumentException("Could not instantiate '" + fqcn + "' class.");
				} catch (IllegalAccessException e) {
					closeSocket();
					throw new IllegalArgumentException("Illegal access to '" + fqcn + "' class.");
				}
				
				closeSocket();
				return;
			}
			
			// Try to find the path in workers map
			if(workersMap.containsKey(path)) {
				workersMap.get(path).processRequest(rc);
				
				closeSocket();
				return;
			}
			
			Path resolvedPath = documentRoot.toAbsolutePath().resolve(Paths.get("./" + path)).toAbsolutePath();
			try {
				resolvedPath = Paths.get(resolvedPath.toFile().getCanonicalPath());
			} catch (IOException e1) {
				closeSocket();
				throw new IllegalArgumentException("Could not open resolvedPath '" + resolvedPath + "'.");
			}
			
			if (!resolvedPath.startsWith(documentRoot)) {
				sendStatusCode(403);
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

	        // Look for SmartScript files (.smscr), if found - execute it
	        if(extension.equals("smscr")) {
	        	String documentBody = "";
	        	
				try {
					Scanner inputScanner = new Scanner(new FileInputStream(requestedFile), "UTF-8");
					while(inputScanner.hasNextLine()) {
						documentBody += inputScanner.nextLine();
					}
					inputScanner.close();
				} catch (FileNotFoundException e) {
					closeSocket();
					throw new IllegalArgumentException("Could not open requestedFile '" + requestedFile + "'.");
				}
				
				try {
					SmartScriptParser parser = new SmartScriptParser(documentBody);
					new SmartScriptEngine(parser.getDocumentNode(), rc).execute();
				} catch (SmartScriptParserException e) {
					closeSocket();
					throw new RuntimeException("Document body could not be parsed.");
				}
				
				closeSocket();
				return;
	        }
	        
	        // Set the document MIME-type
	        String mimeType = "application/octet-stream";
	        if(mimeTypes.containsKey(extension)) {
	        	mimeType = mimeTypes.get(extension);
	        }
	        rc.setMimeType(mimeType);
	        
	        // Return the response
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
			} catch (IOException e) {
				closeSocket();
				throw new IllegalArgumentException("File '" + requestedPath.toFile().getName() + "' could not be opened.");
			}
	        
			closeSocket();
		}

		/**
		 * Check for clients 'sid' cookie and create new one
		 * if it does not exist or it's expired.
		 * 
		 * @param request
		 */
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
				System.out.println("No SID, generating new: " + userEntry.getSid());
			} else {
				int now = (int) (new Date().getTime()/1000);
				
				SessionMapEntry entry = sessions.get(sidCandiate);
				if(entry == null || now > entry.getValidUntil()) {
					userEntry = createNewSidAndCookie();
					System.out.println("SID has expired, generating new: " + userEntry.getSid());
				} else {
					entry.setValidUntil(now + sessionTimeout);
					userEntry = entry;
					System.out.println("SID '" + userEntry.getSid() + "' is valid.");
				}
			}
			
			permPrams = userEntry.getMap();
		}
		
		/**
		 * Create new 'sid' String and add it to the output cookies.
		 * 
		 * @return	Newly generated {@link SessionMapEntry} which contains
		 * 			newly generated 'sid'.
		 */
		private SessionMapEntry createNewSidAndCookie() {
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

			String newSid = "";
			for(int i = 0; i < 20; i++) {
				newSid += alphabet.charAt(sessionRandom.nextInt(alphabet.length()));
			}
			
			int validUntil = (int) (new Date().getTime()/1000) + sessionTimeout;
			
			SessionMapEntry entry = new SessionMapEntry(newSid, validUntil);
			sessions.put(newSid, entry);
			
			outputCookies.add(new RCCookie("sid", newSid, sessionTimeout, address, "/"));
			
			return entry;
		}

		/**
		 * Close the client socket.
		 */
		private void closeSocket() {
			try {
				csocket.close();
			} catch (IOException e) {
				closeSocket();
				throw new RuntimeException("Server socket on port '" + port + "' could not be closed.");
			}
		}

		/**
		 * Send specific HTTP status code.
		 * 
		 * @param statusCode	Status code you want to send.
		 */
		private void sendStatusCode(int statusCode) {
			try {
				ostream.write(getStatusMessage(statusCode).getBytes(StandardCharsets.UTF_8));
				ostream.write((byte) '\n');
				ostream.write((byte) '\n');
			} catch (IOException e) {
				closeSocket();
				throw new RuntimeException("Error when writing to socket output stream.");
			}
		}
		
		/**
		 * Map the status codes with status messages.
		 * 
		 * @param statusCode	Status code you want to retrieve message for.
		 * @return				Status message which corresponds
		 * 						provided status code.
		 */
		private String getStatusMessage(int statusCode) {
			switch(statusCode) {
				case 400: return "HTTP/1.1 400 Bad Request";
				case 403: return "HTTP/1.1 403 Forbidden";
				case 404: return "HTTP/1.1 404 File Not Found";
			}
			
			return null;
		}
		
		/**
		 * Parses the URL parametrs.
		 * 
		 * @param paramString	String which represents URL parameters.
		 */
		private void parseParameters(String paramString) {
			String[] paramsArray = paramString.split("&");
			for(int i = 0; i < paramsArray.length; i++) {
				String[] parameter = paramsArray[i].split("=");
				params.put(parameter[0], parameter[1]);
			}
		}

		/**
		 * Read the client request and saves it into list line-by-line.
		 * 
		 * @param inputStream	Stream form where you want to read the data.
		 * @return				List which represents client request line-by-line.
		 */
		private List<String> readRequest(PushbackInputStream inputStream) {
			List<String> lines = new ArrayList<String>();
			
			Scanner inputScanner = new Scanner(inputStream, "UTF-8");
			while(inputScanner.hasNextLine()) {
				String line = inputScanner.nextLine();
				if(line.isEmpty())
					break;
				lines.add(line);
			}
			
			return lines;
		}
	}
	
	/**
	 * Class which defines client's session map which is identified
	 * by 'sid' String.
	 */
	private static class SessionMapEntry {
		
		String sid;
		long validUntil;
		Map<String, String> map;
		
		/**
		 * Constructor.
		 * 
		 * @param sid			Random String which will be the key for
		 * 						this specific client.
		 * @param validUntil	Set the time when this client's session will
		 * 						expire (in seconds).
		 */
		public SessionMapEntry(String sid, int validUntil) {
			this.sid = sid;
			this.validUntil = validUntil;
			
			map = new ConcurrentHashMap<String, String>();
		}
		
		/**
		 * Set the session valid time.
		 * 
		 * @param validUntil	Session valid time.
		 */
		public void setValidUntil(long validUntil) {
			this.validUntil = validUntil;
		}
		
		/**
		 * @return	Returns session 'sid' string.
		 */
		public String getSid() {
			return sid;
		}
	
		/**
		 * @return	Returns the session valid time (in seconds).
		 */
		public long getValidUntil() {
			return validUntil;
		}
		
		/**
		 * @return	Returns the client's personal data map.
		 */
		public Map<String, String> getMap() {
			return map;
		}
	}
}
