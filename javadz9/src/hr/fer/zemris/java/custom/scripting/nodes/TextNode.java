package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *	Node which represents raw text.
 */
public class TextNode extends Node {

	private String text;

	/**
	 * Creates new TextNode and populate it with text content.
	 * 
	 * @param text Document text.
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * @return Value of the text property
	 */
	public String getValue() {
		return text;
	}
	
	/**
	 * @return Textual representation as it would look like in actual document with proper escaping characters.
	 */
	@Override
	public String asText() {
		return text.replace("[","\\[$");
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}