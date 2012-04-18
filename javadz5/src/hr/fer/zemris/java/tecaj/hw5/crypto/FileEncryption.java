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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class for encryption and decryption a file using AES crypto-algorithm
 * and 128-bit encryption key. 
 */
public class FileEncryption {
	
	/** Use if you want to <b>encrypt</b> a file. */
	public static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;
	/** Use if you want to <b>decrypt</b> a file. */
	public static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

	private Cipher cipher;
	private Path inputPath;
	private Path outputPath;
	
	/**
	 * Create new encrypt/decrypt instance with required parameters.
	 * 
	 * @param sourceFileName		File you want to encrypt/decrypt.
	 * @param destinationFileName	File which will be the result of encryption/decryption.
	 * @param encryptionKey			Encryption key which will be used in process of encryption/decryption.
	 * @param initializationVector	Initialization vector which will be used in process of encryption/decryption.
	 * @param mode					{@link FileEncryption} ENCRYPT_MODE/DECRYPT_MODE.
	 */
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
			System.out.println("No AES/CBC/PKCS5Padding algorithm available.");
		} catch (NoSuchPaddingException e) {
			System.out.println("No such padding.");
		} catch (InvalidKeyException e) {
			System.out.println("Invalid key.");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Invalid algorithm parameters.");
		} catch(IOException e) {
			System.out.println("Could not read a file.");
		}
	}
	
	/**
	 * Do actual encryption/decryption from source to destination file.
	 * 
	 * @return <code>true</code> if file is successfully encrypted/decrypted, otherwise <code>false</code>.
	 */
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
			
			byte[] outputBuffer = new byte[4096];
			int outputByteCount =  cipher.doFinal(outputBuffer, 0);
			outputStream.write(outputBuffer, 0, outputByteCount);
		} catch (ShortBufferException e) {
			return false;
		} catch (IllegalBlockSizeException e) {
			return false;
		} catch (BadPaddingException e) {
			return false;
		} catch (IOException e) {
			return false;
		} 
		
		return true;
	}
	
	/**
	 * Converts hexadecimal notation to byte array.
	 * 
	 * @param hexString		Hex-string you want to convert into <code>byte</code> array.
	 * @return				<code>Byte</code>-array representation of input hexadecimal string.
	 */
	private byte[] hexToByte(String hexString) {
        String part;
        byte[] resultBytes = new byte[hexString.length()/2];
        for (int i = 0; i < hexString.length()/2; i++) {
        	part = hexString.substring(i*2, i*2+2);
        	resultBytes[i] = (byte) (Integer.parseInt(part) & 0xff);
        }
        return resultBytes;
    }
}
