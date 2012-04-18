package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.util.Scanner;

/**
 * Crypto is a program that will allow user to encrypt/decrypt given file using AES crypto-algorithm
 * and 128-bit encryption key or calculate and check SHA-1 file digest.
 */
public class Crypto {

	/**
	 * Provide necessary arguments for program to run. Available commands are:<ul><li>encrypt</li><li>decrypt</li><li>checksha</li></ul>.
	 * 
	 * @param args	Provide a command + additional parameters like input/output files.
	 */
	public static void main(String[] args) {
		if(args.length == 0) throw new IllegalArgumentException("Please provide some of the following commands - 'checksha', 'encrypt' or 'decrypt'.");
		
		if(args[0].equals("checksha")) {
			if(args.length != 2) throw new IllegalArgumentException("Command should have aditional parameter - path of input file.");
			
			checkSHA(args[1]);
		} else if(args[0].equals("encrypt") || args[0].equals("decrypt")) {
			if(args.length != 3) throw new IllegalArgumentException("Command should have two aditional parameters - paths of input and output file.");
			
			fileEncryption(args[1], args[2], args[0]);
		} else {
			throw new IllegalArgumentException("Available commands are 'checksha', 'encrypt' and 'decrypt'.");
		}
	}
	
	/**
	 * Encrypt/decrypt a file using AES crypto-algorithm and 128-bit encryption key.
	 * 
	 * @param inputFileName		File you want to encrypt/decrypt.
	 * @param outputFileName	File which will be the result of encryption/decryption.
	 * @param mode				{@link FileEncryption} ENCRYPT_MODE/DECRYPT_MODE.
	 */
	private static void fileEncryption(String inputFileName, String outputFileName, String mode) {
		String encryptionKey = "";
		String initializationVector = "";
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please provide password as hex-encoded text:");
		encryptionKey = sc.nextLine().trim();
		
		System.out.println("Please provide initialization vector as hex-encoded text:");
		initializationVector = sc.nextLine().trim();
		
		int cipherMode = 0;
		if(mode.equals("encrypt")) {
			cipherMode = FileEncryption.ENCRYPT_MODE;
		} else if(mode.equals("decrypt")) {
			cipherMode = FileEncryption.DECRYPT_MODE;
		}
		
		FileEncryption encryption = new FileEncryption(
				inputFileName,
				outputFileName,
				encryptionKey,
				initializationVector,
				cipherMode
		);
		if(encryption.process()) {
			System.out.println((mode.equals("encrypt") ? "Encryption" : "Decryption") + " completed. " +
					"Generated file " + outputFileName + " based on file " + inputFileName + ".");
		} else {
			System.out.println((mode.equals("encrypt") ? "Encryption" : "Decryption") + " error.");
		}
	}
	
	/**
	 * Calculate and check SHA-1 file digest.
	 * 
	 * @param fileName	File you want to calculate SHA-1 file digest and match it against provided one.
	 */
	private static void checkSHA(String fileName) {
		CheckSHA check = new CheckSHA(fileName);
		check.calculateDigest();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please provide expected sha signature for " + fileName+ ":");
		String expectedHash = sc.nextLine().trim();
		
		if(check.hashCompare(expectedHash)) {
			System.out.println("Digesting completed. Digest of " + fileName + 
					" matches expected digest.");
		} else {
			System.out.println("Digesting completed. Digest of " + fileName + 
					" does not match the expected digest. Digest was: " + check.getDigestAsHexString());
		}
	}

}
