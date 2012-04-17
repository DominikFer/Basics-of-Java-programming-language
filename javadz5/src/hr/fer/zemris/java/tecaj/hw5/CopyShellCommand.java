package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 2) {
			return ShellUtils.error(out, "'copy' command accepts two arguments - file you want to copy and file/directory you want copy to.");
		}
		
		try {
			Path originalFile = Paths.get(arguments[0]);
			if(Files.isDirectory(originalFile)) {
				return ShellUtils.error(out, "First argument is a directory, should be a file.");
			}
			
			if(!Files.exists(originalFile)) {
				return ShellUtils.error(out, "Source file does not exist.");
			}
			
			Path destinationPath = Paths.get(arguments[1]);
			
			if(Files.exists(destinationPath)) {
				if(Files.isDirectory(destinationPath)) {
					destinationPath = Paths.get(arguments[1] += "/" + arguments[0]);
				}
				
				if(Files.exists(destinationPath)) {
					out.write("Destination file already exists, do you want to overwrite it? [YES/NO]");
					out.newLine();
					out.flush();
					
					String answer = in.readLine().toLowerCase();
					if(answer.equals("yes")) {
						Files.copy(originalFile, destinationPath, StandardCopyOption.REPLACE_EXISTING);
					} else if(answer.equals("no")) {
						return ShellUtils.error(out, "No file is copied.");
					} else {
						return ShellUtils.error(out, "Invalid command. No file is copied.");
					}
				} else {
					Files.copy(originalFile, destinationPath);
				}
			} else {
				try {
					Files.copy(originalFile, destinationPath);
				} catch(NoSuchFileException e) {
					Files.createDirectories(destinationPath);
					
					if(!arguments[1].contains(".")) {
						destinationPath = Paths.get(arguments[1] += "/" + arguments[0]);
					}
					Files.copy(originalFile, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			
			out.write("File '" + arguments[0] + "' has been successfully copied to '" + arguments[1] + "'.");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ShellStatus.CONTINUE;
	}
}
