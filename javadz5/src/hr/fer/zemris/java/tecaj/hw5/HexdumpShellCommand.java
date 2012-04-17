package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HexdumpShellCommand implements ShellCommand {

	private static final int BYTE_GROUP_COUNT_PER_ROW = 16;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 1) {
			return ShellUtils.error(out, "'hexdump' command accepts one argument - file you want to hexdump.");
		}
			
		try {
			Path inputFile = Paths.get(arguments[0]);
			InputStream inputStream = new BufferedInputStream(Files.newInputStream(inputFile, StandardOpenOption.READ));
			
			int line = 0;
			
			byte[] inputBuffer = new byte[BYTE_GROUP_COUNT_PER_ROW];
			while(true) {
				int byteCount = inputStream.read(inputBuffer);
				if(byteCount < 1) break;
				
				out.newLine();
				out.write(String.format("%08x", line) + ": " + bytesToHex(inputBuffer, byteCount, BYTE_GROUP_COUNT_PER_ROW));
				
				line += BYTE_GROUP_COUNT_PER_ROW;
			}
			
			out.newLine();
			out.flush();
		} catch (IOException e) {
			return ShellUtils.error(out, "File '" + arguments[0] + "' is not found.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	private String bytesToHex(byte[] bytes, int count, int maxCount) {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	    	if(i >= count) {
	    		sb.append("  ");
	    	} else {
	    		sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    	}
	    	
	    	if(i == maxCount/2-1)
	    		sb.append("|");
	    	else
	    		sb.append(" ");
	    }
	    
	    sb.append("| ");
	    
	    // Alphabet representation
	    for (int i = 0; i < count; i++) {
	    	if(bytes[i] < 37 || bytes[i] > 127) {
	    		sb.append(".");
	    	} else {
	    		sb.append(String.format("%c", bytes[i]));
	    	}
	    }
	    
	    return sb.toString();
	}
}
