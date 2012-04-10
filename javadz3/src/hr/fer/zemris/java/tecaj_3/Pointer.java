package hr.fer.zemris.java.tecaj_3;

public class Pointer<T> {
	
	private T object;
	
	public<X extends T> Pointer(X object) {
		super();
		this.object = object;
	}
	
	/**
	 * @return Returns value of object.
	 */
	public T getObject() {
		return object;
	}
	
	/**
	 * Set the value of object.
	 * 
	 * @param object
	 */
	public <X extends T> void setObject(X object) {
		this.object = object;
	}
}
