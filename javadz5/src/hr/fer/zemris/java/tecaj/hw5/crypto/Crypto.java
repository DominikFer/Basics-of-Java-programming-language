package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.util.Scanner;



public class Crypto {

	public static void main(String[] args) {
		if(args.length == 0) throw new IllegalArgumentException();
		
		if(args[0].equals("checksha")) {
			if(args.length != 2) throw new IllegalArgumentException();
			
			checkSHA(args[1]);
		} else if(args[0].equals("encrypt") || args[0].equals("decrypt")) {
			if(args.length != 3) throw new IllegalArgumentException();
			
			fileEncryption(args[1], args[2]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private static void fileEncryption(String inputFileName, String outputFileName) {
		String encryptionKey = "";
		String initializationVector = "";
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please provide password as hex-encoded text:");
		encryptionKey = sc.nextLine().trim();
		//encryptionKey = "a52217e3ee213ef1ffdee3a192e2ac7e";
		
		System.out.println("Please provide initialization vector as hex-encoded text:");
		initializationVector = sc.nextLine().trim();
		//initializationVector = "000102030405060708090a0b0c0d0e0f";
		
		FileEncryption encryption = new FileEncryption(
				inputFileName,
				outputFileName,
				encryptionKey,
				initializationVector,
				FileEncryption.DECRYPT_MODE
		);
		if(encryption.process()) {
			System.out.println("Decryption completed. Generated file " + inputFileName + " based on file " + outputFileName + ".");
		} else {
			System.out.println("Error.");
		}
	}
	
	private static void checkSHA(String fileName) {
		CheckSHA check = new CheckSHA(fileName);
		check.calculateDigest();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please provide expected sha signature for " + fileName+ ":");
		String expectedHash = sc.nextLine().trim();
		
		if(check.hashCompare(expectedHash)) {
			System.out.println("Digesting completed. Digest of " + fileName + " matches expected digest.");
		} else {
			System.out.println("Digesting completed. Digest of " + fileName + " does not match the expected digest. Digest was: " + check.getDigestAsHexString());
		}
	}

}
