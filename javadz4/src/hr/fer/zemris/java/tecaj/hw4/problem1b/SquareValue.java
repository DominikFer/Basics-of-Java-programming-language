package hr.fer.zemris.java.tecaj.hw4.problem1b;

/**
 * Calculates square value of {@link IntegerStorageChange} value
 * (every time data changes).
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange storageChange) {
		int value = storageChange.getCurrentInteger();
		System.out.println("Provided new value: " + value + ", square is " + value*value);
	}
}
