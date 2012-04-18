package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/***
 * Dumps file content as a hex-values.
 */
public class HexdumpShellCommand implements ShellCommand {

	/** How many byte-groups you want to display per row */
	private static final int BYTE_GROUP_COUNT_PER_ROW = 16;
	
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		if(arguments.length != 1) {
			return MyShell.error(out, "'hexdump' command accepts one argument - file you want to hexdump.");
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
				out.write(String.format("%08x", line) + ": " + bytesToHex(inputBuffer, byteCount, BYTE_GROUP_COUNT_PER_ROW) + alphabeticalRepresentation(inputBuffer, byteCount));
				
				line += BYTE_GROUP_COUNT_PER_ROW;
			}
			
			out.newLine();
			out.flush();
		} catch (FileNotFoundException e) {
			 return MyShell.error(out, "File '" + arguments[0] + "' is not found.");
		} catch (IOException e) {
			System.out.println("Error with I/O buffers.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Returns the alphabetical representation of provided byte-array.
	 * Everything that has value < 37 or > 127 is replaced with the '.' (dot).
	 * 
	 * @param bytes		Byte array you want to display.
	 * @param count		Length of the actual byte array.
	 * @return			Alphabetical representation of the provided byte-array.
	 */
	private String alphabeticalRepresentation(byte[] bytes, int length) {
		StringBuffer buffer = new StringBuffer();
	    for (int i = 0; i < length; i++) {
	    	if(bytes[i] < 37 || bytes[i] > 127) {
	    		buffer.append(".");
	    	} else {
	    		buffer.append(String.format("%c", bytes[i]));
	    	}
	    }
	    
	    return buffer.toString();
	}

	/**
	 * Converts <code>byte</code>-array to hexadecimal string.
	 * 
	 * @param bytes		<code>Byte</code>-array you want to convert.
	 * @return			Hexadecimal representation of the provided byte-array.
	 */
	
	
	/**
	 * Converts <code>byte</code>-array to hexadecimal string
	 * with proper formatting and padding.
	 * 
	 * @param bytes				Byte array you want to display.
	 * @param length			Length of the actual byte array.
	 * @param maxCountPerRow	How many hex-values you want per row?
	 * @return					Hexadecimal representation of the provided byte-array
	 * 							with proper padding.
	 */
	private String bytesToHex(byte[] bytes, int length, int maxCountPerRow) {
		StringBuffer buffer = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	    	if(i < length) {
	    		buffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    	} else {
	    		buffer.append("  ");
	    	}
	    	
	    	if(i == maxCountPerRow/2-1)
	    		buffer.append("|");
	    	else
	    		buffer.append(" ");
	    }
	    
	    return buffer.append("| ").toString();
	}
}
