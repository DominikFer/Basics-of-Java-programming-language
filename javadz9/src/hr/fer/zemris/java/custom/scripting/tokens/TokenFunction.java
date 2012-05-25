package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *	Class which represents mathematical function.
 */
public class TokenFunction extends Token {

	private String name;

	/**
	 * Creates new instance with read-only name.
	 * 
	 * @param name Function name.
	 */
	public TokenFunction(String name) {
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
		return "@" + this.name;
	}
}
