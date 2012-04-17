package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SymbolShellCommand implements ShellCommand {

	private BufferedWriter out;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		this.out = out;
		
		if(arguments.length != 1 && arguments.length != 2) {
			return ShellUtils.error(out, "'symbol' command accepts one or two arguments.");
		}
			
		if(!ShellSymbols.containsSymbolType(arguments[0])) {
			return ShellUtils.error(out, "Unknown '" + arguments[0] + "' symbol.");
		}
		
		if(arguments.length == 2)  {
			if(arguments[1].length() > 1) {
				return ShellUtils.error(out, "New symbol should have the length of 1.");
			}
			
			changeSymbol(arguments[0], arguments[1]);
		} else {
			printSymbol(arguments[0]);
		}
		
		return ShellStatus.CONTINUE;
	}
	
	private void changeSymbol(String name, String symbol) {
		char symbolAfter = symbol.charAt(0);
		
		try {
			char symbolBefore = ShellSymbols.getSymbol(name);
			
			if(name.equals("PROMPT")) {
				ShellSymbols.setPromptSymbol(symbolAfter);
			} else if (name.equals("MORELINES")) {
				ShellSymbols.setMoreLinesSymbol(symbolAfter);
			} else if (name.equals("MULTILINE")) {
				ShellSymbols.setMultiLineSymbol(symbolAfter);
			}
			
			out.write("Symbol for " + name + " changed from '" + symbolBefore + "' to '" + symbolAfter + "'");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printSymbol(String name) {
		try {
			out.write("Symbol for " + name + " is '" + ShellSymbols.getSymbol(name) + "'");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
