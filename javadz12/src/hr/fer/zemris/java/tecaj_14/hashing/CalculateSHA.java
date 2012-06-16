package hr.fer.zemris.java.tecaj_14.hashing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculate and check SHA-1 file digest.
 */
public class CalculateSHA {
	
	private byte[] digest;
	private MessageDigest shaDigest;
	private Path file;
	private String textToHash;
	
	/**
	 * Initialize new instance with <code>SHA-1</code> hash algorithm.
	 * 
	 * @param textToHash	Text you want to calculate or check hash value.
	 */
	public CalculateSHA(String textToHash) {
		this.textToHash = textToHash;
		
		try {
			this.shaDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No SHA-1 algorithm available.");
		}
	}
	
	/**
	 * Initialize new instance with <code>SHA-1</code> hash algorithm.
	 * 
	 * @param fileName	File for which you want to check <code>hash</code> value.
	 */
	public CalculateSHA(Path file) {
		this.file = file;
		
		try {
			this.shaDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No SHA-1 algorithm available.");
		}
	}
	
	/**
	 * Calculate a digest (hash) value only if file could be read, else print error.
	 */
	public CalculateSHA calculateDigest() {
		if (file != null) {
			readFile();
		} else {
			shaDigest.update(textToHash.getBytes());
		}
		
		this.digest = shaDigest.digest();
		
		return this;
	}
	
	/**
	 * Reads a complete file to calculate hash value.
	 * 
	 * @return	<code>true</code> if file is successfully read, <code>false</code> otherwise.
	 */
	private boolean readFile() {
		try (InputStream inputStream = Files.newInputStream(file, StandardOpenOption.READ)) {
			byte[] buffer = new byte[1024];
			while(true) {
				int byteCount = inputStream.read(buffer);
				if(byteCount < 1) break;
				shaDigest.update(buffer, 0, byteCount);
			}
		} catch(IOException ex) {
			System.out.println("Error with reading a file.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * @return	Returns digest/hash value of file.
	 */
	public byte[] getDigest() {
		return this.digest;
	}
	
	/**
	 * Compares instance hash value against provided one.
	 * 
	 * @param hash	Hash you want check against to.
	 * @return		<code>true</code> if hashes are the same, <code>false</code> otherwise.
	 */
	public boolean hashCompare(String hash) {
		return getDigestAsHexString().equals(hash);
	}
	
	/**
	 * @return Returns the digest/hash value as a hexadecimal string.
	 */
	public String getDigestAsHexString() {
		return bytesToHex(this.getDigest());
	}
	
	/**
	 * Converts <code>byte</code>-array to hexadecimal string.
	 * 
	 * @param bytes		<code>Byte</code>-array you want to convert.
	 * @return			Hexadecimal representation of the provided byte-array.
	 */
	private String bytesToHex(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	    	buffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    
	    return buffer.toString();
	}
}
