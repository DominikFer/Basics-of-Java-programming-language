package hr.fer.zemris.java.tecaj.hw5;

/**
 * Class for retrieving and setting <code>MyShell</code>
 * symbol representations.
 */
public class ShellSymbols {

	/** Used on the beginning of every console line (except multilines). */
	public static char PROMPT = '>';
	/** Used if you want to indicate there is more
	 * than a single line of some input. */
	public static char MORELINES = '\\';
	/** Replacement for <code>PROMPT</code> symbol when there is
	 *  multiline user input. */
	public static char MULTILINE = '|';
	
	/**
	 * Returns the current symbol of the <code>MyShell</code>.
	 * 
	 * @param name	Type of the symbol you want to retrieve. Available
	 * ones are: <code>PROMPT</code>, <code>MORELINES</code> and
	 * <code>MULTILINE</code>.
	 * @return		Current symbol representation for that symbol-type.
	 */
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
	
	/**
	 * @param name		
	 * @return			Returns <code>true</code> if the <code>MyShell</code>
	 * 					contains that kind of the symbol type, <code>false</code> otherwise.
	 */
	public static boolean containsSymbolType(String name) {
		if(name.equals("PROMPT") || name.equals("MULTILINE") || name.equals("MORELINES"))
			return true;
		
		return false;
	}
	
	/**
	 * Set the <code>PROMPT</code> symbol to the provided value.
	 * 
	 * @param symbol	New symbol representation for <code>PROMPT</code> symbol.
	 */
	public static void setPromptSymbol(char symbol) {
		PROMPT = symbol;
	}
	
	/**
	 * Set the <code>MORELINES</code> symbol to the provided value.
	 * 
	 * @param symbol	New symbol representation for <code>MORELINES</code> symbol.
	 */
	public static void setMoreLinesSymbol(char symbol) {
		MORELINES = symbol;
	}
	
	/**
	 * Set the <code>MULTILINE</code> symbol to the provided value.
	 * 
	 * @param symbol	New symbol representation for <code>MULTILINE</code> symbol.
	 */
	public static void setMultiLineSymbol(char symbol) {
		MULTILINE = symbol;
	}
}
