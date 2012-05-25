package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents number of type <code>double</code>.
 */
public class TokenConstantDouble extends Token {

	private double value;

	/**
	 * Creates new instance with read-only double value.
	 * 
	 * @param value Constant number.
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * @return Value of value property.
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Double.toString(this.value);
	}
}
