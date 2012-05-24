package hr.fer.zemris.java.hw07.students;

/**
 * Listener which will fire if any change is made in the database.
 */
public interface StudentDBModificationListener {
	
	/**
	 * Students are added to the database from the <code>fromIndex</code>
	 * to the <code>toIndex</code>.
	 * 
	 * @param db			Modified database.
	 * @param fromIndex		Starting index of the change.
	 * @param toIndex		Ending index of the change.
	 */
	public void studentsAdded(StudentDatabase db, int fromIndex, int toIndex);
	
	/**
	 * Students are removed to the database from the <code>fromIndex</code>
	 * to the <code>toIndex</code>.
	 * 
	 * @param db			Modified database.
	 * @param fromIndex		Starting index of the change.
	 * @param toIndex		Ending index of the change.
	 */
	public void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex);
	
	/**
	 * Database change is to complex to display from-to by indexes so treat
	 * it like a new database (refresh all).
	 * 
	 * @param db	Changed database.
	 */
	public void databaseChanged(StudentDatabase db);
	
}