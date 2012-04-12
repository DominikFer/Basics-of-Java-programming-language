package hr.fer.zemris.java.custom.scripting.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class which allows you to store multiple values for the same key and
 * it behaves like a stack.
 */
public class ObjectMultistack {
	
	private Map<String, MultistackEntry> stack = new HashMap<String, MultistackEntry>();
	
	/**
	 * Pushes the <code>value</code> on the stack with the <code>name</code> key.
	 * 
	 * @param name			Stack-key on which you want to push the <code>value</code>.
	 * @param valueWrapper	Value you want to push on the stack.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if(stack.containsKey(name)) {
			stack.get(name).add(valueWrapper);
		} else {
			MultistackEntry entry = new MultistackEntry(valueWrapper);
			stack.put(name, entry);
		}
	}
	
	/**
	 * Pops the <code>value</code> on the stack with the <code>name</code> key.
	 * 
	 * @param name	Stack-key from which you want to pop the last <code>value</code>.
	 * @return		{@link ValueWrapper} value that is popped from the stack.
	 */
	public ValueWrapper pop(String name) {
		if(stack.containsKey(name)) {
			ValueWrapper value = stack.get(name).getLast();
			stack.get(name).removeLast();
			return value;
		} else {
			return null;
		}
	}
	
	/**
	 * Peeks at the last <code>value</code> on the stack with the <code>name</code> key.
	 * 
	 * @param name	Stack-key from which you want to peek the last <code>value</code>.
	 * @return		{@link ValueWrapper} value that is peeked from the stack.
	 */
	public ValueWrapper peek(String name) {
		if(stack.containsKey(name)) {
			return stack.get(name).getLast();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns <code>true</code> or <code>false</code> depending if stack is
	 * empty or not.
	 * 
	 * @param name 	Stack-key for which you want to check if it's empty or not.
	 * @return		<code>true</code> if the stack is empty, else <code>false</code>.
	 */
	public boolean isEmpty(String name) {
		return stack.get(name).size() == 0;
	}
	
	/**
	 * Class that acts like a stack entry.
	 */
	class MultistackEntry {
		
		private List<ValueWrapper> list = new LinkedList<ValueWrapper>();
	    private int size;
		
	    /**
	     *  Creates new {@link MultistackEntry} object.
	     */
	    public MultistackEntry() {}
	    
	    /**
	     * Creates new {@link MultistackEntry} with initial value.
	     * 
	     * @param value	Initial value added to the this entry.
	     */
	    public MultistackEntry(ValueWrapper value) {
			this.add(value);
		}
	    
	    /**
	     * Adds value to the entry.
	     * 
	     * @param value	Value you want to add.
	     */
	    public void add(ValueWrapper value) {
	    	list.add(value);
	    	size++;
	    }
	    
	    /**
	     * Removes the value from the entry.
	     * 
	     * @param index	Index from which you want to remove an object.
	     * @throws IllegalArgumentException if the list is empty, or <code>index</code> is invalid.
	     */
	    private void remove(int index) {
	    	if(size == 0) throw new IllegalArgumentException("Cannot remove, list is empty.");
	    	
	    	if(index >= 0 && index < size) {
	    		list.remove(index);
	    		size--;
	    	} else {
	    		throw new IllegalArgumentException("Invalid index! There is no object at that position.");
	    	}
	    }
	    
	    /**
	     * Retrieves the value from the entry.
	     * 
	     * @param index	Index from which you want to retrieve an object.
	     * @throws IllegalArgumentException if the list is empty, or <code>index</code> is invalid.
	     */
	    private ValueWrapper get(int index) {
	    	if(size == 0) throw new IllegalArgumentException("Cannot retrieve, list is empty.");
	    	
	    	if(index >= 0 && index < size) {
	    		return this.list.get(index);
	    	} else {
	    		throw new IllegalArgumentException("Invalid index! There is no object at that position.");
	    	}
	    }
	    
	    /**
	     * Returns the last value from this entry.
	     * 
	     * @return The last value from entry.
	     */
	    public ValueWrapper getLast() {
	    	return this.get(this.size-1);
	    }
	    
	    /**
	     * Remove the last value from this entry.
	     */
	    public void removeLast() {
	    	this.remove(this.size-1);
	    }
	    
	    /**
	     * @return The entry size.
	     */
	    public int size() {
			return this.size;
		}
	}
}
