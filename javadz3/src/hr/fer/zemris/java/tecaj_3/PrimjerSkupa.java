package hr.fer.zemris.java.tecaj_3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * <p><b>Set</b> collection doesn't allow duplicates so any of the listings
 * does not have duplicates in it.</p>
 * 
 * <p><b>HashSet</b> in background works with the help of hash functions so it's hashing values
 * and also we don't have sorted order (as we were adding elements in the set).</p>
 * 
 * <p><b>TreeSet</b> works with the help of tree-structure and all elements are sorted
 * (in our case in alphabetical order because we have words).</p>
 *  
 * <p><b>LinkedHashSet</b> is the same as the HashSet but has build-in List which
 * tracks in which order elements are added to the Set. As a result we
 * have element printout exactly as the elements were added to the set.</p>
 *  
 * <p>We are printing elements with the help of shortened notation (ispisiSkup2 is the
 * same thing but in longer notation). We can do that because all our collections
 * implement {@link Iterable} interface.</p>
 *
 */
public class PrimjerSkupa {

	public static void main(String[] args) {
		System.out.println("Preko HashSet-a:");
		ispisiSkup(ukloniDuplikate1(args));
		System.out.println();
		
		System.out.println("Preko TreeSet-a:");
		ispisiSkup(ukloniDuplikate2(args));
		System.out.println();
		
		System.out.println("Preko LinkedHashSet-a:");
		ispisiSkup(ukloniDuplikate3(args));
		System.out.println();
	}
	
	private static void ispisiSkup(Collection<String> col) {
		for(String element : col) {
			System.out.println(element);
		}
	}

	private static void ispisiSkup2(Collection<String> col) {
		Iterator<String> iterator = col.iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	private static Collection<String> ukloniDuplikate1(String[] polje) {
		Set<String> set = new HashSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		
		return set;
	}
	
	private static Collection<String> ukloniDuplikate2(String[] polje) {
		Set<String> set = new TreeSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		
		return set;
	}
	
	private static Collection<String> ukloniDuplikate3(String[] polje) {
		Set<String> set = new LinkedHashSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		
		return set;
	}
}
