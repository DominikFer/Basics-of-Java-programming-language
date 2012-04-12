package hr.fer.zemris.java.custom.scripting.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectMultistack {
	
	private Map<String, MultistackEntry> stack = new HashMap<String, MultistackEntry>();
	
	public void push(String name, ValueWrapper valueWrapper) {
		if(stack.containsKey(name)) {
			stack.get(name).add(valueWrapper);
		} else {
			MultistackEntry node = new MultistackEntry(valueWrapper);
			stack.put(name, node);
		}
	}
	
	public ValueWrapper pop(String name) {
		if(stack.containsKey(name)) {
			ValueWrapper value = stack.get(name).getLast();
			stack.get(name).removeLast();
			return value;
		} else {
			return null;
		}
	}
	
	public ValueWrapper peek(String name) {
		if(stack.containsKey(name)) {
			return stack.get(name).getLast();
		} else {
			return null;
		}
	}
	
	public boolean isEmpty(String name) {
		return stack.get(name).size() == 0;
	}
	
	class MultistackEntry {
		
		private List<ValueWrapper> list = new LinkedList<ValueWrapper>();
	    private int size;
		
	    public MultistackEntry(ValueWrapper value) {
			this.add(value);
		}
	    
	    public void add(ValueWrapper value) {
	    	list.add(value);
	    	size++;
	    }
	    
	    private void remove(int index) {
	    	if(index >= 0 && index < size) {
	    		list.remove(index);
	    	}
	    	size--;
	    }
	    
	    private ValueWrapper get(int index) {
	    	return this.list.get(index);
	    }
	    
	    public ValueWrapper getLast() {
	    	return this.get(this.size-1);
	    }
	    
	    public void removeLast() {
	    	this.remove(this.size-1);
	    }
		
		public int size() {
			return this.size;
		}
	}
}
