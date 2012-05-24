package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *	Main document node, all other nodes are children of this one
 */
public class DocumentNode extends Node {
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
	
}
