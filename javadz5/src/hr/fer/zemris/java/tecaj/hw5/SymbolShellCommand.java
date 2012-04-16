package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SymbolShellCommand implements ShellCommand {

	private BufferedWriter out;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		this.out = out;
		
		// Should be reusable by other classes
		while(arguments.length > 0 && arguments[arguments.length-1].equals(Character.toString(MyShell.getMoreLinesSymbol()))) {
			try {
				System.out.print(MyShell.getMultiLineSymbol() + " ");
				String newLine = in.readLine();
				arguments = updateArguments(arguments, newLine.split(" "));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(arguments.length == 2) {
				changeSymbol(arguments[0], arguments[1].charAt(0));
			} else if(arguments.length == 1) {
				printSymbol(arguments[0]);
			} else {
				throw new IllegalArgumentException();
			}
			
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ShellStatus.CONTINUE;
	}
	
	private void changeSymbol(String name, char symbolAfter) {
		try {
			char symbolBefore = MyShell.getSymbol(name);
			
			if(name.equals("PROMPT")) {
				MyShell.setPromptSymbol(symbolAfter);
			} else if (name.equals("MORELINES")) {
				MyShell.setMoreLinesSymbol(symbolAfter);
			} else if (name.equals("MULTILINE")) {
				MyShell.setMultiLineSymbol(symbolAfter);
			} else {
				throw new IllegalArgumentException();
			}
			
			out.write("Symbol for " + name + " changed from '" + symbolBefore + "' to '" + symbolAfter + "'");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printSymbol(String name) {
		try {
			out.write("Symbol for " + name + " is '" + MyShell.getSymbol(name) + "'");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] updateArguments(String[] arguments, String[] newLine) {
		String[] expandedArguments = new String[arguments.length-1 + newLine.length];
		System.arraycopy(arguments, 0, expandedArguments, 0, arguments.length-1);
		System.arraycopy(newLine, 0, expandedArguments, arguments.length-1, newLine.length);
		
		return expandedArguments;
	}
}
