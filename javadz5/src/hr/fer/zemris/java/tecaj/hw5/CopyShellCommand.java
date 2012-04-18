package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Copy a file to another file or directory. 
 */
public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 2) {
			return MyShell.error(out, "'copy' command accepts two arguments - file you want to copy and file/directory you want copy to.");
		}
		
		try {
			Path originalFile = Paths.get(arguments[0]);
			
			if(!Files.exists(originalFile)) {
				return MyShell.error(out, "Source file does not exist.");
			}
			
			if(Files.isDirectory(originalFile)) {
				return MyShell.error(out, "First argument is a directory, should be a file.");
			}
			
			Path destinationPath = Paths.get(arguments[1]);
			String destinationPathString = destinationPath.toString();
			
			if (!Files.exists(destinationPath) && !Files.exists(destinationPath.getParent())) {
				return MyShell.error(out, "Destination directory does not exist.");
			} else if (Files.isDirectory(destinationPath)) {
				if (!destinationPathString.endsWith(File.separator)) {
					destinationPathString += File.separator;
				}
				destinationPath = Paths.get(destinationPathString += originalFile.getFileName());
			}
	            
			if (Files.exists(destinationPath) && Files.isRegularFile(destinationPath)) {
				out.write("Destination file already exists, do you want to overwrite it? [Y/n]");
				out.newLine();
				out.flush();
				
				String answer = in.readLine();
				if(answer.equals("n")) {
					return MyShell.error(out, "No file is copied.");
				} else if (!answer.equals("Y")) {
					return MyShell.error(out, "Invalid command. No file is copied.");
				}
			}

			Files.copy(originalFile, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			
			out.write("File '" + arguments[0] + "' has been successfully copied to '" + destinationPathString + "'.");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Error with I/O buffers.");
		}
		
		return ShellStatus.CONTINUE;
	}
}
