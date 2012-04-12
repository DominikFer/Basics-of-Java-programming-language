package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class IntegerStorageChange {
	
	private int pastInteger;
	private int currentInteger;
	private IntegerStorage storage;
	
	public IntegerStorageChange(IntegerStorage storage, int pastInteger, int currentInteger) {
		this.storage = storage;
		this.pastInteger = pastInteger;
		this.currentInteger = currentInteger;
	}

	public int getPastInteger() {
		return pastInteger;
	}

	public int getCurrentInteger() {
		return currentInteger;
	}

	public IntegerStorage getStorage() {
		return storage;
	}
}
