package hr.fer.zemris.java.tecaj_3;

import java.util.Iterator;

/**
 * Personal implementation of the simple LinkedList.
 * You can only add/get objects and iterate through the list.
 * 
 * @param <T>
 */
public class LinkedList<T> implements Iterable<T> {

	private ListNode<T> startNode;
	private ListNode<T> lastNode;
	private int size = 0;
	
	/**
	 * Adds object at the end of the list.
	 * 
	 * @param o Object you want to add.
	 */
    public void add(T o) {
    	if(this.startNode == null) {
    		this.startNode = new ListNode<T>(o);
    		this.lastNode = this.startNode;
    	} else {
    		this.lastNode.nextNode = new ListNode<T>(o);
    		this.lastNode = this.lastNode.nextNode;
    	}
    	size++;
    }
    
    /**
     * Returns the object at specified position (index) in the list.
     * 
     * @param index Position in the list of returning object.
     * @return Object at specified index (can be <code>null</code>) or <code>null</code> if index is invalid (there is no object at specified index).
     */
    public T get(int index) {
    	ListNode<T> node = this.startNode;
    	if(node == null) return null;
    	
    	for(int i = 1; i <= index; i++) {
    		if(node == null) return null;
    		node = node.nextNode;
    	}
    	
    	return node.element;
    }
    
    /**
     * @return The size of the list.
     */
    public int size() {
    	return this.size;
    }

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int counter = 0;
			
			@Override
			public boolean hasNext() {
				return (counter < size);
			}

			@Override
			public T next() {
				return get(counter++);
			}

			@Override
			public void remove() {
				// Do nothing
			}
		};
	}
}