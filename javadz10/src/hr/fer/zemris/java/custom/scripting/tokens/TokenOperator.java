package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents mathematical symbol (supported +,-,*,/).
 */
public class TokenOperator extends Token {

	private String symbol;

	/**
	 * Creates new instance with read-only symbol.
	 * 
	 * @param symbol Symbol of mathematical operator.
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * @return Value of symbol property.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return this.symbol;
	}
}
