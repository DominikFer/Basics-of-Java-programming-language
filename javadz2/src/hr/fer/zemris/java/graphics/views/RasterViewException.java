package hr.fer.zemris.java.graphics.views;

/**
 * Thrown when there is an error in parsing user commands or rendering shapes.
 */
public class RasterViewException extends Exception {

	private static final long serialVersionUID = -7822455197809160433L;

	public RasterViewException(){ 
		super();
	}
	
	public RasterViewException(String message){ 
		super(message);
	}

	public RasterViewException(String message, Throwable throwable){ 
		super(message, throwable); 
	}
}
