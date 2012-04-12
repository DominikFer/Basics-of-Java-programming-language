package hr.fer.zemris.java.tecaj.hw4.problem1a;

/**
 * Class which counts how many times data changed
 * in {@link IntegerStorage} class.
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/** Variable which stores how many times data changed
	 * in {@link IntegerStorage} class. */
	private int counter;
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}
