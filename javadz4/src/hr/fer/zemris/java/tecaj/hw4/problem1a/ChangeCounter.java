package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class ChangeCounter implements IntegerStorageObserver {

	private int counter;
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}
