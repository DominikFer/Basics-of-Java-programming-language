package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * MyShell which provides following commands:
 * <ul>
 * <li><code>symbol</code> - change <code>PROMPT</code>,
 * <code>MULTILINE</code> or <code>MORELINES</code> symbol.</li>
 * <li><code>charsets</code> - get the list of the all available
 * charsets on this machine.</li>
 * <li><code>cat</code> - write the content of the given file to the console.</li>
 * <li><code>ls</code> - list the provided directory.</li>
 * <li><code>tree</code> - print a tree-list of provided directory.</li>
 * <li><code>copy</code> -  copy a file to another file or directory.</li>
 * <li><code>mkdir</code> - make a new directory.</li>
 * <li><code>hexdump</code> - dumps file content as a hex-values.</li>
 * <li><code>exit</code> - exits the console.</li>
 * </ul>
 * @version 1.0 
 */
public class MyShell {

	private static Map<String, ShellCommand> commands = new HashMap<String, ShellCommand>();
	
	/**
	 * Run the new <code>MyShell</code> instance.
	 * 
	 * @param args	Program arguments (irrelevant).
	 */
	public static void main(String[] args) {
		commands.put("exit", new ExitShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("charsets", new CharsetShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			out.write("Welcome to MyShell v 1.0");
			out.newLine();
			out.flush();
		} catch (IOException e1) {
			System.out.println("Error with writing to output buffer.");
		}
		
		ShellStatus status = ShellStatus.CONTINUE;
		while(status == ShellStatus.CONTINUE) {
			try {
				out.write(ShellSymbols.PROMPT + " ");
				out.flush();
				
				String[] userInput = readLines(in);
				
				String commandName = userInput[0];
				String[] commandArgs = Arrays.copyOfRange(userInput, 1, userInput.length);
				
				ShellCommand command = commands.get(commandName);
				
				if(command == null) {
					out.write("Unknown command.");
					out.newLine();
					out.flush();
				} else {
					status = command.executeCommand(in, out, commandArgs);
				}
			} catch (IOException e) {
				System.out.println("Error with reading from/to input/output.");
			}
		}
	}
	
	/**
	 * Reads one or more lines if the line of the user input if it's
	 * finishing with ' \' (default) value.
	 * 
	 * @param in	Wrapped <code>stdin</code> reader.
	 * @return		User input as a string array (splitted by spaces).
	 */
	private static String[] readLines(BufferedReader in) {
		String line = "";
		
		try {
			line = in.readLine();
			
			while(line.endsWith(" " + Character.toString(ShellSymbols.MORELINES))) {
				System.out.print(ShellSymbols.MULTILINE + " ");
				String newLine = in.readLine();
				line = line.substring(0, line.length()-1);
				line += newLine;
			}
		} catch (IOException e) {
			System.out.println("Error with reading from input buffer.");
		}
		
		return line.split(" ");
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
