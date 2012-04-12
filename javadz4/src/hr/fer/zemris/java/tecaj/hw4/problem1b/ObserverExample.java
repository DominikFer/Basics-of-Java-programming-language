package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class ObserverExample {

	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		
		IntegerStorageObserver squareValueObserver = new SquareValue();
		IntegerStorageObserver changeObserver = new ChangeCounter();
		
		istorage.addObserver(squareValueObserver);
		istorage.addObserver(changeObserver);
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}

}
