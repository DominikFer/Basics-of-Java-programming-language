package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 *	Representation of the single node in document. 
 */
public class Node {

	private ArrayBackedIndexedCollection children;
	
	/**
	 * Add's child to this node.
	 * 
	 * @param child Node you want to add as a child.
	 */
	public void addChildNode(Node child) {
		if(this.children == null) {
			this.children = new ArrayBackedIndexedCollection();
		}
		
		this.children.add(child);
	}
	
	/**
	 * @return Number of children.
	 */
	public int numberOfChildren() {
		if(this.children == null) {
			return 0;
		}
		
		return this.children.size();
	}

	/**
	 * Returns the node at index position.
	 * 
	 * @param index Position of the requested Node.
	 * @return Node at the index position.
	 */
	public Node getChild(int index) {
		return (Node) this.children.get(index);
	}
	
	/**
	 * @return Textual representation as it would look like in real document (including all tags and tokens).
	 */
	public String asText() {
		return "";
	}
}
