package hr.fer.zemris.java.tecaj.hw5;

public class ShellSymbols {

	public static char PROMPT = '>';
	public static char MORELINES = '\\';
	public static char MULTILINE = '|';
	
	public static char getSymbol(String name) {
		if(name.equals("PROMPT")) {
			return PROMPT;
		} else if(name.equals("MORELINES")) {
			return MORELINES;
		} else if(name.equals("MULTILINE")) {
			return MULTILINE;
		} else {
			return ' ';
		}
	}
	
	public static boolean containsSymbolType(String name) {
		if(name.equals("PROMPT") || name.equals("MULTILINE") || name.equals("MORELINES"))
			return true;
		
		return false;
	}
	
	public static void setPromptSymbol(char symbol) {
		PROMPT = symbol;
	}
	
	public static void setMoreLinesSymbol(char symbol) {
		MORELINES = symbol;
	}
	
	public static void setMultiLineSymbol(char symbol) {
		MULTILINE = symbol;
	}
}
