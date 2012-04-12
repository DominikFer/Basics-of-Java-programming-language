package hr.fer.zemris.java.tecaj.hw4.problem1a;

/**
 * Class which stores integer value and can register
 * {@link IntegerStorageObserver} observer.
 */
public class IntegerStorage {
	
	private int value;
	private IntegerStorageObserver observer;
	
	/**
	 * Creates {@link IntegerStorage} class with <code>initialValue</code>.
	 * 
	 * @param initialValue Initial value you want to store in this class.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	/**
	 * Sets the class observer to <code>observer</code>.
	 * 
	 * @param observer Observer you want to register.
	 */
	public void setObserver(IntegerStorageObserver observer) {
		this.observer = observer;
	}
	
	/**
	 * Clears the class observer.
	 */
	public void clearObserver() {
		this.observer = null;
	}
	
	/**
	 * @return Returns the value of {@link IntegerStorage} value.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Sets the {@link IntegerStorage} value to <code>value</code>.
	 * Observer method <code>valueChanged</code> is called if possible.
	 * 
	 * @param value Value you want to set.
	 */
	public void setValue(int value) {
		if(this.value != value) {
			this.value = value;
			if(observer != null) {
				observer.valueChanged(this);
			}
		}
	}
}
