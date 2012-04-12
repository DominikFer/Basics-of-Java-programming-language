package hr.fer.zemris.java.tecaj.hw4.problem1b;

/**
 * Observer for {@link IntegerStorageChange} class. Defines actions which need
 * to be implemented in order to execute a certain action whenever object
 * data changes.
 */
public interface IntegerStorageObserver {
	
	/**
	 * Action which will be called whenever data in
	 * {@link IntegerStorageChange} changes.
	 * 
	 * @param istorage Class which you want to observe.
	 */
	public void valueChanged(IntegerStorageChange istorage);
}
