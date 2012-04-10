package hr.fer.zemris.java.tecaj_3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Program reads two files (given as parameters - file name and extension) and displays
 * only lines which exist in first file, but not in the second one. 
 *
 */
public class Names {
	
	private static String firstFileName;
	private static String secondFileName;
	
	public static void main(String[] args) {
		
		try {
			getFileNames(args);
		} catch (IllegalArgumentException e) {
			System.out.println("Program prima 2 argumenta (imena datoteka).");
			return;
		}
		
		// Define Sets and retrieve lines from both files
		Set<String> firstFileNames = new HashSet<String>();
		Set<String> secondFileNames = new HashSet<String>();
		
		try {
			readFile(firstFileName, firstFileNames);
			readFile(secondFileName, secondFileNames);
		}  catch (FileNotFoundException e) {
			System.out.println("Datoteka nije pronaðena.");
		}  catch (IOException e) {
			System.out.println("Problem sa èitanjem datoteke.");
		}
		
		// Printout
		for(String line : firstFileNames) {
			if(!secondFileNames.contains(line)) {
				System.out.println(line);
			}
		}
	}
	
	/**
	 * Retrieves file names from program arguments.
	 * 
	 * @param args Two arguments - two file names.
	 */
	private static void getFileNames(String[] args) {
		if(args.length != 2) {
			throw new IllegalArgumentException();
		}
		
		firstFileName = args[0];
		secondFileName = args[1];
	}
	
	/**
	 * Retrieves lines from file and put them in given Set collection.
	 * 
	 * @param fileName File name from which you want to read.
	 * @param namesList Set collection which will save all the lines from the file.
	 * @throws FileNotFoundException thrown if file is not found (invalid name, extension...).
	 * @throws IOException thrown if there is a problem with a file and therefore it could not be read.
	 */
	private static void readFile(String fileName, Set<String> namesList) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new BufferedInputStream(new FileInputStream(fileName)), "UTF-8"));
		
		while(true) {
			String line = null;
			line = br.readLine();
			if(line == null) break;
			line = line.trim();
			if(line.isEmpty()) continue;
			
			namesList.add(line);
		}
		
		br.close();
	}
}
