package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyShell {

	private static Map<String, ShellCommand> commands = new HashMap<String, ShellCommand>();
	
	private static char PROMPT = '>';
	private static char MORELINES = '\\';
	private static char MULTILINE = '|';
	
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
			e1.printStackTrace();
		}
		
		ShellStatus status = ShellStatus.CONTINUE;
		while(status == ShellStatus.CONTINUE) {
			try {
				out.write(PROMPT + " ");
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
				e.printStackTrace();
			}
		}
	}
	
	private static String[] readLines(BufferedReader in) {
		String line = "";
		
		try {
			line = in.readLine();
			
			while(line.endsWith(" " + Character.toString(MyShell.getMoreLinesSymbol()))) {
				System.out.print(MyShell.getMultiLineSymbol() + " ");
				String newLine = in.readLine();
				line = line.substring(0, line.length()-1);
				line += newLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line.split(" ");
	}
	
	public static char getSymbol(String name) {
		if(name.equals("PROMPT")) {
			return getPromptSymbol();
		} else if(name.equals("MORELINES")) {
			return getMoreLinesSymbol();
		} else if(name.equals("MULTILINE")) {
			return getMultiLineSymbol();
		} else {
			return ' ';
		}
	}
	
	public static boolean containsSymbolType(String name) {
		if(name.equals("PROMPT") || name.equals("MULTILINE") || name.equals("MORELINES"))
			return true;
		
		return false;
	}

	public static char getPromptSymbol() {
		return PROMPT;
	}
	
	public static void setPromptSymbol(char symbol) {
		PROMPT = symbol;
	}
	
	public static char getMoreLinesSymbol() {
		return MORELINES;
	}
	
	public static void setMoreLinesSymbol(char symbol) {
		MORELINES = symbol;
	}
	
	public static char getMultiLineSymbol() {
		return MULTILINE;
	}
	
	public static void setMultiLineSymbol(char symbol) {
		MULTILINE = symbol;
	}
}
