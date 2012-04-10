package hr.fer.zemris.java.tecaj_3;

import java.util.Iterator;

/**
 * Create prime collection from 0 to <code>numberOfPrimes</code>. 
 */
public class PrimesCollection implements Iterable<Integer> {

	private int numberOfPrimes;
	private LinkedList<Integer> list;
	
	/**
	 * Creates new prime-numbers list from 0 to <code>numberOfPrimes</code>.
	 * 
	 * @param numberOfPrimes Maximum number of calculated prime numbers.
	 */
	public PrimesCollection(int numberOfPrimes) {
		this.numberOfPrimes = numberOfPrimes;
		this.list = new LinkedList<Integer>();
		
		populateWithPrimes();
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return this.list.iterator();
	}
	
	/**
	 * Populate the list with prime numbers.
	 * 
	 * @param index Position of the prime number.
	 * @return prime number at specified index
	 */
	public int populateWithPrimes() {
		int nPrimes = 0;
		boolean primNumber;
		
		if(this.numberOfPrimes == 0)
			return 0;
		
		for(int i = 2; i > 0; i++) {
			primNumber = true;
			for(int j = 2; j < i; j++) {
				if(i % j == 0) {
					primNumber = false;
					break;
				}
			}
			if(primNumber) {
				list.add(i);
				if(++nPrimes == this.numberOfPrimes)
					break;
			}
		}
		
		return 0;
	}
}