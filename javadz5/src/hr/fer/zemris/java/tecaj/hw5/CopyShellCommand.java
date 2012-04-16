package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 2) {
			return ShellUtils.error(out, "'copy' command accepts two arguments - file you want to copy and file/directory you want copy to.");
		}
		
		Path directory = Paths.get(arguments[0]);
		try {
			Files.createDirectories(directory);
			
			out.write("New directory '" + arguments[0] + "' successfully created.");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			return ShellUtils.error(out, "Error: could not create new directory '" + arguments[0] + "'.");
		}
		
		return ShellStatus.CONTINUE;
	}
}
