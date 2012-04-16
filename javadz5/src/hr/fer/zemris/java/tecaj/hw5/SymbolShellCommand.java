package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SymbolShellCommand implements ShellCommand {

	private BufferedWriter out;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		this.out = out;
		
		arguments = ShellUtils.checkIfMultiLine(in, arguments);
		
		if(arguments.length != 1 && arguments.length != 2) {
			return ShellUtils.error(out, "'symbol' command should have one or two arguments.");
		}
			
		if(!MyShell.containsSymbolType(arguments[0])) {
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
			char symbolBefore = MyShell.getSymbol(name);
			
			if(name.equals("PROMPT")) {
				MyShell.setPromptSymbol(symbolAfter);
			} else if (name.equals("MORELINES")) {
				MyShell.setMoreLinesSymbol(symbolAfter);
			} else if (name.equals("MULTILINE")) {
				MyShell.setMultiLineSymbol(symbolAfter);
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
			out.write("Symbol for " + name + " is '" + MyShell.getSymbol(name) + "'");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
