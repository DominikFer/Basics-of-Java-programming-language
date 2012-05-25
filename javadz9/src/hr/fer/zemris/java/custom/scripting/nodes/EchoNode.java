package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 *	EchoNode or so called empty-tag. Contains one or more tokens.
 */
public class EchoNode extends Node {

	private Token[] tokens;

	/**
	 * Create EchoNode with the set of tokens.
	 * 
	 * @param tokens Tokens/parameters of this node.
	 */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}

	/** 
	 * @return Returns all the tokens
	*/
	public Token[] getTokens() {
		return tokens;
	}
	
	/**
	 * @return Textual representation as it would look like in actual document (includes all tokens).
	 */
	public String asText() {
		String body = "[$=";
		for(int i = 0; i < this.tokens.length; i++) {
			body += this.tokens[i].asText() + " ";
		}
		body = body.substring(0, body.length()-1); // Remove last space
		return body + " $]";
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
