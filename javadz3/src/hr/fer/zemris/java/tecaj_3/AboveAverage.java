package hr.fer.zemris.java.tecaj_3;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AboveAverage {
	
	/** Sum of all elements in the list. */
	private static double total;
	
	public static void main(String[] args) {
		
		// Use LinkedList because at the end numbers must be ordered from
		// lower to higher ones
		List<Double> numbersList = new LinkedList<Double>();
		readNumbers(numbersList);
		
		double average = total/numbersList.size();
		
		for(double number : numbersList) {
			if(number * 1.2 >= average) {
				System.out.println(number);
			}
		}
	}

	/**
	 * Reads numbers (integers and doubles) from console and inserts
	 * them into List and calculates total of all elements in the list.
	 * 
	 * @param numbersList List in which you want to insert numbers.
	 */
	private static void readNumbers(List<Double> numbersList) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String line = sc.nextLine().trim();
			if(line.equals("quit")) break;
			
			try {
				double number = Double.parseDouble(line);
				numbersList.add(number);
				
				total += number;
			} catch (NumberFormatException e) {
				// Just ignore it
			}
		}
	}
}
