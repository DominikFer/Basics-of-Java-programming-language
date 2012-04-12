package hr.fer.zemris.java.custom.scripting.demo;

import java.util.HashMap;
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
		MultistackEntry entry = null;
		if(stack.containsKey(name)) {
			entry = new MultistackEntry(valueWrapper, stack.get(name));
			stack.put(name, entry);
		} else {
			entry = new MultistackEntry(valueWrapper, null);
		}
		
		stack.put(name, entry);
	}
	
	/**
	 * Pops the <code>value</code> on the stack with the <code>name</code> key.
	 * 
	 * @param name	Stack-key from which you want to pop the last <code>value</code>.
	 * @return		{@link ValueWrapper} value that is popped from the stack.
	 */
	public ValueWrapper pop(String name) {
		if(stack.containsKey(name)) {
			ValueWrapper value = stack.get(name).getValue();
			MultistackEntry entry = stack.get(name);
			if(entry.getNextEntry() != null) {
				stack.put(name, entry.getNextEntry());
			} else {
				stack.remove(name);
			}
			return value;
		} else {
			throw new IllegalArgumentException("Stack with name '" + name + "' is empty.");
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
			return stack.get(name).getValue();
		} else {
			throw new IllegalArgumentException("Stack with name '" + name + "' is empty.");
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
		return !stack.containsKey(name);
	}
	
	/**
	 * Class that acts like a node of single-linked list.
	 */
	class MultistackEntry {
		
		private MultistackEntry nextEntry;
		private ValueWrapper value;
		
	    /**
	     * Creates new {@link MultistackEntry} with initial value and <code>nextEntry</code>.
	     * 
	     * @param value		Initial value added to the this entry.
	     * @param nextEntry	Next entry in the list.
	     */
	    public MultistackEntry(ValueWrapper value, MultistackEntry nextEntry) {
			this.value = value;
			this.nextEntry = nextEntry;
		}
	    
	    /**
	     * @return The next entry from the current entry (can be <code>null</code>).
	     */
	    public MultistackEntry getNextEntry() {
	    	return this.nextEntry;
	    }
	    
	    /**
	     * @return	Returns the value of current {@link MultistackEntry}.
	     */
	    public ValueWrapper getValue() {
	    	return this.value;
	    }
	}
}
