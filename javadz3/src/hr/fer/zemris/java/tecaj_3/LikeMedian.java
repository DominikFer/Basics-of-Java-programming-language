package hr.fer.zemris.java.tecaj_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which will after adding some elements in it, on <code>get</code> method return
 * the median element (if the number of elements is even, method will return the smaller
 * one from the two elements which would usually be used to calculate median element).
 * 
 * @param <T> Object type in the list.
 */
public class LikeMedian<T extends Comparable<T>> {
	
	private List<T> list;
	private boolean sorted;
	
	public<X extends T> LikeMedian() {
		this.list = new ArrayList<T>();
		this.sorted = true;
	}
	
	/**
	 * Adds object at the end of the list.
	 * 
	 * @param object Object you want to add in the list.
	 */
	public void add(T object) {
		list.add(object);
		this.sorted = false;
	}
	
	/**
	 * @return Median element from the list (if the number of elements is even,
	 * method will return the smaller one from the two elements which would usually
	 * be used to calculate median element).
	 */
	public T get() {
		if(!sorted) {
			Collections.sort(this.list);
			this.sorted = true;
		}
		
		int size = this.list.size();
		if(size % 2 == 0) {
			return list.get(size/2 - 1);
		} else {
			return list.get(size/2);
		}
	}
}
