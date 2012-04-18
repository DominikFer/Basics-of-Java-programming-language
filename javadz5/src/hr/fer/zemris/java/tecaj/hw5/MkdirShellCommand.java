package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Make a new directory. 
 */
public class MkdirShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 1) {
			return MyShell.error(out, "'mkdir' command accepts one arguments - directory you want to create.");
		}
		
		Path directory = Paths.get(arguments[0]);
		try {
			Files.createDirectories(directory);
		} catch (IOException e) {
			return MyShell.error(out, "Error: could not create new directory '" + arguments[0] + "'.");
		}
		
		try {
			out.write("New directory '" + arguments[0] + "' successfully created.");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Error with output buffer.");
		}
		
		return ShellStatus.CONTINUE;
	}
}
