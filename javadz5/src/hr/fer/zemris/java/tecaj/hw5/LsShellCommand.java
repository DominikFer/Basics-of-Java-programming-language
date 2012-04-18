package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * List the provided directory (root-level only).
 */
public class LsShellCommand implements ShellCommand {

	private BufferedWriter out;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		this.out = out;
		
		if(arguments.length != 1) {
			return MyShell.error(out, "'ls' command accepts one arguments - directory you want to list.");
		}
		
		Path directory = Paths.get(arguments[0]);
		if(!directory.toFile().isDirectory()) {
			return MyShell.error(out, "'" + arguments[0] + "' is not a directory.");
		}
		
		try {
			Files.walkFileTree(directory, new DirectoryVisitor());
		} catch (IOException e) {
			return MyShell.error(out, "Error with IO operation.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * {@link FileVisitor} class which prints the actual content of the directory. 
	 */
	private class DirectoryVisitor implements FileVisitor<Path> {
		
		private int depth;
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
			depth--;
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attra) throws IOException {
			String directoryName = dir.toFile().getName();
			long directorySize = getDirectorySize(dir.toFile());
			
			if(depth > 0) printLine(getPermissions(dir), directorySize, getFormattedDate(dir), directoryName);

			depth++;
			
			if(depth > 1)
				return FileVisitResult.SKIP_SUBTREE;
			else
				return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
			String fileName = file.toFile().getName();
			long fileSize = Files.size(file);
			
			printLine(getPermissions(file), fileSize, getFormattedDate(file), fileName);
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException e) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Returns the directory size.
		 * 
		 * @param dir	Directory for which you want calculate it's size.
		 * @return		Size of the provided directory.
		 */
		private long getDirectorySize(File dir) {
			long length = 0;
		    for (File file : dir.listFiles()) {
		        if (file.isFile())
		            length += file.length();
		        else
		            length += getDirectorySize(file);
		    }
		    return length;
		}
		
		/**
		 * Returns the file/directory permissions as a string ('drwx').
		 * If something is not available it's replaced with the dash '-' for ex.
		 * if it's not readable it's 'd-wx'.
		 * 
		 * @param path		File/directory for which you want check the permissions.
		 * @return			'drwx' representation of the permissions.
		 */
		private String getPermissions(Path path) {
			char directory = '-';
			if(Files.isDirectory(path)) directory = 'd';
			
			char readable = '-';
			if(Files.isReadable(path)) readable = 'r';
			
			char writeable = '-';
			if(Files.isWritable(path)) writeable = 'w';
			
			char executable = '-';
			if(Files.isExecutable(path)) executable = 'x';
			
			return new StringBuilder().append(directory).append(readable).append(writeable).append(executable).toString();
		}
		
		/**
		 * Prints the actual line to the console.
		 * 
		 * @param permissions			Permission of the file (formatted as 'drwx').
		 * @param fileSize				File size.
		 * @param formattedDateTime		File creation date (formatted).
		 * @param fileName				File name.
		 */
		private void printLine(String permissions, long fileSize, String formattedDateTime, String fileName) {
			try {
				out.write(String.format("%s %10d %s %s", permissions, fileSize, formattedDateTime, fileName));
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println("Error with output buffer.");
			}
		}
		
		/**
		 * Returns the file creation date properly formatted.
		 * 
		 * @param path			
		 * @return				
		 * @throws IOException	
		 */
		private String getFormattedDate(Path path) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				BasicFileAttributeView faView = Files.getFileAttributeView(path,
						BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
				BasicFileAttributes attributes = faView.readAttributes();
				FileTime fileTime = attributes.creationTime();
				return sdf.format(new Date(fileTime.toMillis()));
			} catch (IOException e) {
				MyShell.error(out, "File attributed (creation date) could not be read.");
				return "";
			}
		}
	}
}
