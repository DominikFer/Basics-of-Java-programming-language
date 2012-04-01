package hr.fer.zemris.java.custom.collections;

public class ObjectStack {
	
	/**
	 * Adaptee class.
	 */
	private ArrayBackedIndexedCollection array;
	
	/**
	 * Creates new object stack.
	 */
	public ObjectStack() {
		this.array = new ArrayBackedIndexedCollection();
	}
	
	/**
	 * Checks if stack is empty.
	 * 
	 * @return <code>true</code> if stack is empty, else <code>false</code>.
	 */
	public boolean isEmpty() {
		return this.array.isEmpty();
	}
	
	/**
	 * Returns number of currently stored objects in stack.
	 * 
	 * @return Stack size.
	 */
	public int size() {
		return this.array.size();
	}
	
	/**
	 * Pushes given <code>value</code> on the stack.
	 * 
	 * @param value Object you want to push.
	 * @throws IllegalArgumentException <code>value</code> cannot be <code>null</code>.
	 */
	public void push(Object value) {
		if(value == null)
			throw new IllegalArgumentException();
		
		this.array.add(value);
	}
	
	/**
	 * Removes last value pushed on stack from stack and returns it.
	 * 
	 * @return Last value pushed on stack.
	 * @throws EmptyStackException Cannot <code>pop</code> empty stack.
	 */
	public Object pop() {
		if(this.array.size() == 0)
			throw new EmptyStackException("Cannot pop beacuse stack is empty.");
		
		Object value = this.array.get(this.array.size()-1);
		this.array.remove(this.array.size()-1);
		
		return value;
	}
	
	/**
	 * Returns last element pushed on stack, but does not delete it.
	 * 
	 * @return Last element pushed on stack.
	 * @throws EmptyStackException Cannot <code>pop</code> empty stack.
	 */
	public Object peek() {
		if(this.array.size() == 0)
			throw new EmptyStackException("Cannot pop beacuse stack is empty.");
		
		return this.array.get(this.array.size()-1);
	}
	
	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		this.array.clear();
	}
}
