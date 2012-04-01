package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Thrown when there is an error in document and therefore it cannot be parsed.
 */
public class SmartScriptParserException extends Exception {

	private static final long serialVersionUID = -7146405271722626823L;

	public SmartScriptParserException(){ 
		super();
	}
	
	public SmartScriptParserException(String message){ 
		super(message);
	}

	public SmartScriptParserException(String message, Throwable throwable){ 
		super(message, throwable); 
	}
}
