package hr.fer.zemris.java.tecaj_13.web;

/**
 * Class for escaping HTML characters.
 */
public class HTMLSupport {

	public static String escapeForHTMLBody(String text) {
		if(text == null) return "";
		
		text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		return text;
	}
	
	public static String escapeForTagAttribute(String text) {
		return escapeForHTMLBody(text);
	}
	
}
