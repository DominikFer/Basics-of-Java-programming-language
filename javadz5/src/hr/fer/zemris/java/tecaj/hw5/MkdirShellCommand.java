package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MkdirShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		arguments = ShellUtils.checkIfMultiLine(in, arguments);
		
		if(arguments.length != 1) {
			return ShellUtils.error(out, "'mkdir' command accepts one arguments - directory you want to create.");
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
