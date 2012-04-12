package hr.fer.zemris.java.tecaj.hw4.problem1b;

import java.util.HashSet;
import java.util.Set;

/**
 * Class which stores integer value and can register multiple
 * {@link IntegerStorageObserver} observers.
 */
public class IntegerStorage {
	
	private int value;
	private Set<IntegerStorageObserver> observers = new HashSet<IntegerStorageObserver>();
	
	/**
	 * Creates {@link IntegerStorage} class with <code>initialValue</code>.
	 * 
	 * @param initialValue Initial value you want to store in this class.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	/**
	 * Adds the observer to the list of registered observers.
	 * 
	 * @param observer Observer you want to add.
	 */
	public void addObserver(IntegerStorageObserver observer) {
		this.observers.add(observer);
	}
	
	/**
	 * Removes the observer from the list of registered observers.
	 * 
	 * @param observer Observer you want to remove.
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if(this.observers.contains(observer)) {
			this.observers.remove(observer);
		}
	}
	
	/**
	 * Clears all class observers.
	 */
	public void clearObserver() {
		this.observers.clear();
	}
	
	/**
	 * @return Returns the value of {@link IntegerStorage} value.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Sets the {@link IntegerStorage} value to <code>value</code>.
	 * Observers method <code>valueChanged</code> is called if possible
	 * (all added observers are called).
	 * 
	 * @param value Value you want to set.
	 */
	public void setValue(int value) {
		if(this.value != value) {
			IntegerStorageChange change = new IntegerStorageChange(this, this.value, value);
			
			this.value = value;
			for(IntegerStorageObserver observer : this.observers) {
				if(observer != null) {
					observer.valueChanged(change);
				}
			}
		}
	}
}
