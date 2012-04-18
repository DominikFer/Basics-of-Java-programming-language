package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Print a tree-list of provided directory. 
 */
public class TreeShellCommand implements ShellCommand {

	private BufferedWriter out;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		this.out = out;
		
		if(arguments.length != 1) {
			return MyShell.error(out, "'tree' command accepts one arguments - directory you want to list.");
		}
		
		Path directory = Paths.get(arguments[0]);
		if(!directory.toFile().isDirectory()) {
			return MyShell.error(out, "'" + arguments[0] + "' is not a directory.");
		}
		
		try {
			Files.walkFileTree(directory, new PrintTree());
		} catch (IOException e) {
			return MyShell.error(out, "Error with I/O operations.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Class which runs through the directory printing out out all the files and sub-directories. 
	 */
	private class PrintTree implements FileVisitor<Path> {
		
		int depth;
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
			depth--;
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attra) throws IOException {
			if(depth == 0) {
				System.out.println(dir.toFile().getAbsolutePath());
			} else {
				System.out.println(String.format("%" + (depth*2) + "s", "") + dir.toFile().getName());
			}
			
			depth++;
			
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
			String fileName = file.toFile().getName();
			
			out.write(String.format("%" + (depth*2) + "s", "") + fileName);
			out.newLine();
			out.flush();
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException e) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
}
