package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption {
	
	public static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;
	public static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

	private Cipher cipher;
	private Path inputPath;
	private Path outputPath;
	
	public FileEncryption(String sourceFileName, String destinationFileName, String encryptionKey, String initializationVector, int mode) {
		SecretKeySpec keySpec = new SecretKeySpec(hexToByte(encryptionKey), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(initializationVector));
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(mode, keySpec, paramSpec);
			
			inputPath = Paths.get(sourceFileName);
			File outputFile = new File(destinationFileName);
			outputPath = outputFile.toPath();
			if(!outputFile.exists()) {
				Files.createFile(outputPath);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean process() {
		try {
			InputStream inputStream = Files.newInputStream(inputPath, StandardOpenOption.READ);
			OutputStream outputStream = Files.newOutputStream(outputPath, StandardOpenOption.WRITE);
			
			byte[] inputBuffer = new byte[4096];
			while(true) {
				int byteCount = inputStream.read(inputBuffer);
				if(byteCount < 1) break;
				
				byte[] outputBuffer = new byte[4096];
				int outputByteCount = cipher.update(inputBuffer, 0, byteCount, outputBuffer);
				outputStream.write(outputBuffer, 0, outputByteCount);
			}
		} catch (IOException e) {
			return false;
		} catch (ShortBufferException e) {
			return false;
		}
		
		return true;
	}
	
	private byte[] hexToByte(String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte) (Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
    }
}
