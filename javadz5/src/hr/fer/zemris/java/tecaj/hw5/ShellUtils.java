package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ShellUtils {
	public static String[] checkIfMultiLine(BufferedReader in, String[] arguments) {
		while(arguments.length > 0 && arguments[arguments.length-1].equals(Character.toString(MyShell.getMoreLinesSymbol()))) {
			try {
				System.out.print(MyShell.getMultiLineSymbol() + " ");
				String newLine = in.readLine();
				arguments = updateArguments(arguments, newLine.split(" "));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return arguments;
	}
	
	private static String[] updateArguments(String[] arguments, String[] newLine) {
		String[] expandedArguments = new String[arguments.length-1 + newLine.length];
		System.arraycopy(arguments, 0, expandedArguments, 0, arguments.length-1);
		System.arraycopy(newLine, 0, expandedArguments, arguments.length-1, newLine.length);
		
		return expandedArguments;
	}
	
	public static ShellStatus error(BufferedWriter out, String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ShellStatus.CONTINUE;
	}
}
