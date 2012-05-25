package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents variable.
 */
public class TokenVariable extends Token {

	private String name;

	/**
	 * Creates new instance with read-only variable name.
	 * 
	 * @param name Variable name.
	 */
	public TokenVariable(String name) {
		this.name = name;
	}
	
	/**
	 * @return Value of name property.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return this.name;
	}
}
