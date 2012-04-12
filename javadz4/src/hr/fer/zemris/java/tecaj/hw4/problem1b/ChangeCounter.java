package hr.fer.zemris.java.tecaj.hw4.problem1b;

/**
 * Class which counts how many times data changed
 * in {@link IntegerStorageChange} class.
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/** Variable which stores how many times data changed
	 * in {@link IntegerStorageChange} class. */
	private int counter;
	
	@Override
	public void valueChanged(IntegerStorageChange storageChange) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}
