package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class CatShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		arguments = ShellUtils.checkIfMultiLine(in, arguments);
		
		if(arguments.length != 1 && arguments.length != 2) {
			return ShellUtils.error(out, "'cat' command accepts one or two arguments.");
		}
		
		Charset charset = Charset.defaultCharset();
		if(arguments.length == 2) {
			try {
				charset = Charset.forName(arguments[1]);	
			} catch (IllegalCharsetNameException e) {
				return ShellUtils.error(out, "Charset name is not valid.");
			} catch (UnsupportedCharsetException e) {
				return ShellUtils.error(out, "Charset '" + arguments[1] + "' is not supporeted on this machine.");
			}
		}
		
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
					new BufferedInputStream(
					new FileInputStream(arguments[0])), charset));
			
			String line = null;
			while((line = br.readLine()) != null) {
				out.write(line);
				out.flush();
			}
			
			out.newLine();
			out.flush();
		} catch (IOException e) {
			return ShellUtils.error(out, "File '" + arguments[0] + "' is not found.");
		}
		
		return ShellStatus.CONTINUE;
	}
}
