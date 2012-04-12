package hr.fer.zemris.java.tecaj.hw4.problem1a;

/**
 * Calculates square value of {@link IntegerStorage} value
 * (every time data changes).
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		System.out.println("Provided new value: " + value + ", square is " + value*value);
	}


}
