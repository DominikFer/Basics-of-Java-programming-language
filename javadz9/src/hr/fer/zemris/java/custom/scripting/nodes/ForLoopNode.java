package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 *	Node used as FOR-loop. 
 */
public class ForLoopNode extends Node {

	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;

	/**
	 * Create forLoopNode with 4 parameters.
	 * 
	 * @param variable Variable used in for loop.
	 * @param startExpression Starting expression, can be any Token type.
	 * @param endExpression Ending expression, can be any Token type.
	 * @param stepExpression Step expression, can be any Token type and also can be <code>null</code>.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression, Token endExpression, Token stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * @return Returns value of <code>variable</code> property.
	 */
	public TokenVariable getVariable() {
		return variable;
	}

	/**
	 * @return Returns value of <code>start expression</code> property.
	 */
	public Token getStartExpression() {
		return startExpression;
	}

	/**
	 * @return Returns value of <code>end expression</code> property.
	 */
	public Token getEndExpression() {
		return endExpression;
	}

	/**
	 * @return Returns value of <code>step expression</code> property.
	 */
	public Token getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * @return Returns textual representation as it would look like in actual document (includes all parameters except the step expression if it's <code>null</code>).
	 */
	@Override
	public String asText() {
		String body = "[$ FOR " + this.variable.asText() + " " + this.startExpression.asText() + " " + this.endExpression.asText() + " ";
		if(stepExpression != null) {
			body += this.stepExpression.asText() + " ";
		}
		
		return body + "$]";
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
