package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents <code>String</code> type of data.
 */
public class TokenString extends Token {

	private String value;

	/**
	 * Creates new instance with read-only <code>String</code> value.
	 * 
	 * @param value String value.
	 */
	public TokenString(String value) {
		this.value = value;
	}
	
	/**
	 * @return Value of value property.
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return "\"" + this.value + "\"";
	}
}
