package hr.fer.zemris.java.tecaj_14.web;

/**
 * Class for escaping HTML characters.
 */
public class HTMLSupport {

	/**
	 * Escapes <, > and & characters in the provided <code>text</code>.
	 * 
	 * @param text	Text you want to escape.
	 * @return		Escaped HTML text.
	 */
	public static String escapeForHTMLBody(String text) {
		if(text == null) return "";
		
		text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		return text;
	}
	
}
