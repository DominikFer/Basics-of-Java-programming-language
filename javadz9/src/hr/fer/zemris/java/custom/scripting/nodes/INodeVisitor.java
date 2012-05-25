package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *	Node visitor interface used to define visit's to
 *	{@link Node} classes.
 */
public interface INodeVisitor {

	/**
	 * Method used to visit {@link TextNode} instances via
	 * Visitor design pattern.
	 * 
	 * @param node	{@link TextNode} you want to visit (add
	 * 				additional functinality).
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Method used to visit {@link ForLoopNode} instances via
	 * Visitor design pattern.
	 * 
	 * @param node	{@link ForLoopNode} you want to visit (add
	 * 				additional functinality).
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Method used to visit {@link EchoNode} instances via
	 * Visitor design pattern.
	 * 
	 * @param node	{@link EchoNode} you want to visit (add
	 * 				additional functinality).
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Method used to visit {@link DocumentNode} instances via
	 * Visitor design pattern.
	 * 
	 * @param node	{@link DocumentNode} you want to visit (add
	 * 				additional functinality).
	 */
	public void visitDocumentNode(DocumentNode node);
	
}
