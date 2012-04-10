package hr.fer.zemris.java.tecaj_3;

/**
 * Used as a single node in LinkedList implementation.
 * 
 * @param <T>
 */
public class ListNode<T> {

	public T element;
	public ListNode<T> nextNode;
    
	public ListNode(T element) {
		this(element, null);
	}
    
	public ListNode(T element, ListNode<T> nextNode) {
    	this.element = element;
    	this.nextNode = nextNode;
	}
}