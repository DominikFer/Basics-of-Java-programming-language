package hr.fer.zemris.java.tecaj_3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Program reads names from console and on "quit" prints out all the names and
 * how many times they occurred.
 *
 */
public class NamesCounter {

	public static void main(String[] args) {
		
		Map<String, Integer> names = new HashMap<String, Integer>();
		readNames(names);
		
		for(Entry<String, Integer> entry : names.entrySet()) {
			System.out.println("Ime '" + entry.getKey() + "' se pojavilo " + entry.getValue() + " puta.");
		}
	}

	/**
	 * Read names from console and inserts them into Map with {@link Integer} value which remembers how
	 * many times specific name was entered.
	 * 
	 * @param names Map in which you want to insert the names.
	 */
	private static void readNames(Map<String, Integer> names) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String name = sc.nextLine().trim();
			if(name.equals("quit")) break;
			if(name.length() == 0) continue;
			
			if(names.containsKey(name)) {
				names.put(name, names.get(name).intValue()+1);
			} else {
				names.put(name, 1);
			}
		}
	}
}
