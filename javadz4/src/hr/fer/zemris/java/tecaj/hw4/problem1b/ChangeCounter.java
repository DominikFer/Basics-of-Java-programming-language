package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class ChangeCounter implements IntegerStorageObserver {

	private int counter;
	
	@Override
	public void valueChanged(IntegerStorageChange storageChange) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}
