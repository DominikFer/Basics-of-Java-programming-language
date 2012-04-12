package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class IntegerStorage {
	
	private int value;
	private IntegerStorageObserver observer;
	
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	public void setObserver(IntegerStorageObserver observer) {
		this.observer = observer;
	}
	
	public void clearObserver() {
		this.observer = null;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		if(this.value != value) {
			this.value = value;
			if(observer != null) {
				observer.valueChanged(this);
			}
		}
	}
}
