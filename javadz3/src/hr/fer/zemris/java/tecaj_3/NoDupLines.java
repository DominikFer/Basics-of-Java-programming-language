package hr.fer.zemris.java.tecaj_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Program receives line-by-line in console and when empty line is entered program will
 * reverse the order of inserted lines and remove all duplicates. After that,
 * lines will be printed out. 
 *
 */
public class NoDupLines {
	
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>();
		
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String line = sc.nextLine().trim();
			if(line.length() == 0) break;
			
			list.add(line);
		}
		
		Object[] reversed = reverseAndRemoveDuplicates(list.toArray());
		int size = reversed.length;
		
		for(int i = 0; i < size; i++) {
			if(reversed[i] != null)
				System.out.println(reversed[i]);
		}
	}
	
	/**
	 * Method for reversing array order and removing duplicates from it.
	 * 
	 * @param array Array which will be reversed and won't have any duplicates.
	 * @return Array with reversed order of elements and will not contain duplicates.
	 */
	private static Object[] reverseAndRemoveDuplicates(Object[] array) {
		Object[] reversedArray = new String[array.length];
		
		for(int i = array.length-1; i >= 0; i--) {
			if(!containsObject(reversedArray, array[i])) {
				reversedArray[array.length-1 - i] = array[i];
			}
		}
		
		return reversedArray;
	}
	
	/**
	 * Checks if array contains provided object or not.
	 * 
	 * @param array Array which will be checked for <code>Object o</code>.
	 * @param o Object which you are looking for.
	 * @return <code>TRUE</code> if array contains provided object, <code>FALSE</code> otherwise (or object is <code>null</code>).
	 */
	private static boolean containsObject(Object[] array, Object o) {
		int size = array.length;
		if(o != null) {
			for(int i = 0; i < size; i++) {
				if (o.equals(array[i])) return true;
			}
		}
		
		return false;
	}
}