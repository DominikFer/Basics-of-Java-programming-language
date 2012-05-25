package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>Class which manages HTTP headers, response codes/messages,
 * remembering parameters or persistent parameters and
 * cookies.</p>
 * <p>Used to send HTTP Response from the server to the client.</p>
 */
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
	
	/**
	 * Constructor.
	 * 
	 * @param outputStream				Stream where all the response content will be written.
	 * @param parameters				Parameters stored within this context.
	 * @param persistentParameters		Persistent parameters stored within this context.
	 * @param outputCookies				Cookies which will be sent out within response headers.
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		if(outputStream == null)
			throw new IllegalArgumentException("RequestContext outputStream cannot be 'null'.");
		
		this.outputStream = outputStream;
		
		if(parameters == null)
			parameters = new HashMap<String, String>();
		else
			this.parameters = parameters;
		
		if(persistentParameters == null)
			persistentParameters = new HashMap<String, String>();
		else
			this.persistentParameters = persistentParameters;
		
		if(outputCookies == null)
			outputCookies = new ArrayList<RCCookie>();
		else
			this.outputCookies = outputCookies;
	}

	/**
	 * Set the context encoding.
	 * 
	 * @param encoding			Encoding you want to use.
	 * @throws RuntimeException	If trying to set the encoding while header is already generated.
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated)
			throw new RuntimeException("You cannot modify 'encoding' because header is already generated.");
		
		this.encoding = encoding;
	}

	/**
	 * Set the response HTTP Status Code.
	 * 
	 * @param statusCode		Status code you want to send.
	 * @throws RuntimeException	If trying to set the status code while header is already generated.
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated)
			throw new RuntimeException("You cannot modify 'status code' because header is already generated.");
		
		this.statusCode = statusCode;
	}

	/**
	 * Set the response HTTP Status Message (related with statusCode).
	 * 
	 * @param statusText		Status message you want to send.
	 * @throws RuntimeException	If trying to set the status message while header is already generated.
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated)
			throw new RuntimeException("You cannot modify 'status text' because header is already generated.");
		
		this.statusText = statusText;
	}

	/**
	 * Set the response mime-type.
	 * 
	 * @param mimeType	Mime-type you want to use.
	 * @throws RuntimeException	If trying to set the mime-type while header is already generated.
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated)
			throw new RuntimeException("You cannot modify 'mime type' because header is already generated.");
		
		this.mimeType = mimeType;
	}
	
	
	/**
	 * Add a new cookie.
	 * 
	 * @param cookie			New cookie you want to add.
	 * @throws RuntimeException	If trying to add a cookie while header is already generated.
	 */
	public void addRCCookie(RCCookie cookie) {
		if(headerGenerated)
			throw new RuntimeException("You cannot add 'output cookie' because header is already generated.");
		
		outputCookies.add(cookie);
	}
	
	/**
	 * @return Returns the map of the parameters.
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * @return Returns the map of the persistent parameters.
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}
	
	/**
	 * Set the persistent parameters map.
	 * 
	 * @param persistentParameters	Persistent parameters map you want to set.
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}
	
	/**
	 * Returns the parameter with provided name.
	 * 
	 * @param name		Parameter name
	 * @return			Returns the parameter value for
	 * 					provided name or <code>null</code>
	 * 					if it does not exist.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns the persistent parameter with provided name.
	 * 
	 * @param name		Persistent parameter name
	 * @return			Returns the persistent parameter value for
	 * 					provided name or <code>null</code>
	 * 					if it does not exist.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Stores new value into persistent parameters map.
	 * 
	 * @param name		Name of the new persistent parameter.
	 * @param value		Value of the new persistent parameter.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes the persistent parameter from the map if it
	 * exists.
	 * 
	 * @param name	Persistent parameter name.
	 */
	public void removePersistentParameter(String name) {
		if(persistentParameters.containsKey(name)) persistentParameters.remove(name);
	}
	
	/**
	 * @return	Returns parameter names list.
	 */
	public Set<String> getParameterNames() {
		Set<String> names = new HashSet<String>();
		for(Entry<String, String> parameter : parameters.entrySet()) {
			names.add(parameter.getKey());
		}
		return names;
	}
	
	/**
	 * @return Returns persistent parameter names list.
	 */
	public Set<String> getPersistentParameterNames() {
		Set<String> names = new HashSet<String>();
		for(Entry<String, String> parameter : persistentParameters.entrySet()) {
			names.add(parameter.getKey());
		}
		return names;
	}
	
	/**
	 * Writes the byte content into outputStream.
	 * 
	 * @param data				Data you want to write.
	 * @return					Current instance.
	 * @throws IOException		Thrown if data could not be written to the
	 * 							output stream.
	 */
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}
		
		outputStream.write(data);
		
		return this;
	}
	
	/**
	 * Writes the byte content into outputStream with specific offset and length.
	 * 
	 * @param data			Data you want to write.
	 * @param offset		The start offset in the data.
	 * @param length		The number of bytes to write.
	 * @return				Current instance.
	 * @throws IOException	Thrown if data could not be written to the
	 * 						output stream.
	 */
	public RequestContext write(byte[] data, int offset, int length) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}
		
		outputStream.write(data, offset, length);
		
		return this;
	}
	
	/**
	 * Writes the text content (encoded with previously defined encoding
	 * or with default 'UTF-8') to the output stream.
	 * 
	 * @param text			Text you want to write.
	 * @return				Current instance.
	 * @throws IOException	Thrown if data could not be written to the
	 * 						output stream.
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader().getBytes(charset));
		}

		outputStream.write(text.getBytes(charset));
		
		return this;
	}
	
	/**
	 * Generates new header from current parameters.
	 * 
	 * @return	String representing the new header.
	 */
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
		
		header += "\r\n";
		header += "\r\n";
		
		charset = Charset.forName(encoding);
		headerGenerated = true;
		
		return header;
	}
	
	/**
	 *	Class used to define cookie.
	 */
	public static class RCCookie {
		
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		/**
		 * Constructor.
		 * 
		 * @param name		Cookie name.
		 * @param value		Cookie value.
		 * @param maxAge	Cookie max-age.
		 * @param domain	Cookie domain.
		 * @param path		Cookie path.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * @return Returns the cookie name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return Returns the cookie value.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return Returns the cookie domain.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return Returns the cookie path.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return Returns the cookie max-age.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}

}
