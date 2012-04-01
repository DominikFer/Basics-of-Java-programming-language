package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents number of type <code>int</code>.
 */
public class TokenConstantInteger extends Token {

	private int value;

	/**
	 * Creates new instance with read-only int value.
	 * 
	 * @param value Constant number.
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * @return Value of value property.
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Integer.toString(this.value);
	}
}
