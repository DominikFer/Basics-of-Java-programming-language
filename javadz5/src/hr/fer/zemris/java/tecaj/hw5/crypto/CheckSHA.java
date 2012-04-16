package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CheckSHA {
	
	private byte[] digest;
	private MessageDigest shaDigest;
	private String fileName;
	
	public CheckSHA(String fileName) {
		this.fileName = fileName;
		
		try {
			this.shaDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void calculateDigest() {
		readFile();
		
		this.digest = shaDigest.digest();
	}
	
	private void readFile() {
		Path p = Paths.get(fileName);
		try (InputStream inputStream = Files.newInputStream(p, StandardOpenOption.READ)) {
			byte[] buffer = new byte[1024];
			while(true) {
				int byteCount = inputStream.read(buffer);
				if(byteCount < 1) break;
				shaDigest.update(buffer, 0, byteCount);
			}
		} catch(IOException ex) {
			
		}
	}
	
	public byte[] getDigest() {
		return this.digest;
	}
	
	public boolean hashCompare(String hash) {
		return getDigestAsHexString().equals(hash);
	}
	
	public String getDigestAsHexString() {
		return bytesToHex(this.getDigest());
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	    	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    
	    return sb.toString();
	}
}
