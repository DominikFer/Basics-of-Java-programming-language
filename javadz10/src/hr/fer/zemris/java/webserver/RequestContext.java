package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RequestContext {

	private OutputStream outputStream;
	private Charset charset;
	
	private String encoding = "UTF-8";
	private int statusCode = 200;
	private String statusText = "OK";
	private String mimeType = "text/html";
	
	private Map<String, String> parameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated = false;
	
	public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this.outputStream = outputStream;
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
	}

	public void setEncoding(String encoding) {
		if(headerGenerated) throw new RuntimeException("You cannot modify 'encoding' because header is already generated.");
		
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		if(headerGenerated) throw new RuntimeException("You cannot modify 'status code' because header is already generated.");
		
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		if(headerGenerated) throw new RuntimeException("You cannot modify 'status text' because header is already generated.");
		
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		if(headerGenerated) throw new RuntimeException("You cannot modify 'mime type' because header is already generated.");
		
		this.mimeType = mimeType;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}
	
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	public void removePersistentParameter(String name) {
		if(persistentParameters.containsKey(name)) persistentParameters.remove(name);
	}
	
	public Set<String> getParameterNames() {
		Set<String> names = new HashSet<String>();
		for(Entry<String, String> parameter : parameters.entrySet()) {
			names.add(parameter.getKey());
		}
		return names;
	}
	
	public Set<String> getPersistentParameterNames() {
		Set<String> names = new HashSet<String>();
		for(Entry<String, String> parameter : persistentParameters.entrySet()) {
			names.add(parameter.getKey());
		}
		return names;
	}
	
	public void addRCCookie(RCCookie cookie) {
		if(headerGenerated) throw new RuntimeException("You cannot add 'output cookie' because header is already generated.");
		
		outputCookies.add(cookie);
	}
	
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}
		
		outputStream.write(data);
		
		return this;
	}
	
	public RequestContext write(byte[] data, int offset, int length) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}
		
		outputStream.write(data, offset, length);
		
		return this;
	}
	
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}

		outputStream.write(text.getBytes(charset));
		
		return this;
	}
	
	private String generateHeader() {
		String header = "";
		
		header += "HTTP/1.1 " + statusCode + " " + statusText;
		header += "\r\n";
		header += "Content-Type: " + mimeType;
		if(mimeType.startsWith("text/")) header += "; charset=" + encoding;
		for(RCCookie cookie : outputCookies) {
			header += "\r\n";
			header += "Set-Cookie: " + cookie.getName() + "=\"" + cookie.getValue() + "\";";
			if(cookie.getDomain() != null) header += " Domain=" + cookie.getDomain() + ";";
			if(cookie.getPath() != null) header += " Path=" + cookie.getPath() + ";"; 
			if(cookie.getMaxAge() != null) header += " Max-Age=" + cookie.getMaxAge() + ";";
			header = header.substring(0, header.length()-1);
		}
		
		header += "\r\n\r\n";
		
		charset = Charset.forName(encoding);
		headerGenerated = true;
		
		return header;
	}
	
	public static class RCCookie {
		
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public Integer getMaxAge() {
			return maxAge;
		}
	}

}
