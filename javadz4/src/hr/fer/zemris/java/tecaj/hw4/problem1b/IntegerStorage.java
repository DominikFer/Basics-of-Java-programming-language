package hr.fer.zemris.java.tecaj.hw4.problem1b;

import java.util.HashSet;
import java.util.Set;

public class IntegerStorage {
	
	private int value;
	private Set<IntegerStorageObserver> observers = new HashSet<IntegerStorageObserver>();
	
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	public void addObserver(IntegerStorageObserver observer) {
		this.observers.add(observer);
	}
	
	public void removeObserver(IntegerStorageObserver observer) {
		if(this.observers.contains(observer)) {
			this.observers.remove(observer);
		}
	}
	
	public void clearObserver() {
		this.observers.clear();
	}
	
	public int getValue() {
		return this.value;
	}
	
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
