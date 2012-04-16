package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

public class CharsetShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		arguments = ShellUtils.checkIfMultiLine(in, arguments);
		
		if(arguments.length != 0) {
			return ShellUtils.error(out, "'charsets' command doesn't accept any argument.");
		}
		
		Map<String, Charset> charsets = Charset.availableCharsets();
		
		try {
			for(Entry<String, Charset> charset : charsets.entrySet()) {
				out.write(charset.getKey());
				out.newLine();
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ShellStatus.CONTINUE;
	}
}
