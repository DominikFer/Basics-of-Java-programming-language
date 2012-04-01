package hr.fer.zemris.java.custom.collections;

/**
 * Thrown when the stack is empty and stack.pop()/peek() is called
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 5019575938076748881L;
	
	public EmptyStackException(){ 
		super();
	}
	
	public EmptyStackException(String message){ 
		super(message);
	}

	public EmptyStackException(String message, Throwable throwable){ 
		super(message, throwable); 
	}
}
