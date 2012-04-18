package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Returns the list of the all available charsets on this machine. 
 */
public class CharsetShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 0) {
			return MyShell.error(out, "'charsets' command doesn't accept any argument.");
		}
		
		Map<String, Charset> charsets = Charset.availableCharsets();
		
		try {
			for(Entry<String, Charset> charset : charsets.entrySet()) {
				out.write(charset.getKey());
				out.newLine();
				out.flush();
			}
		} catch (IOException e) {
			System.out.println("Error with I/O buffers.");
		}
		
		return ShellStatus.CONTINUE;
	}
}
