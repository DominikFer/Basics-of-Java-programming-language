package hr.fer.zemris.java.hw07.students;

/**
 * Interface which defines methods for using student database
 * (with {@link Student} records).
 */
public interface StudentDatabase {

	/**
	 * @return Returns the number of students.
	 */
	public int size();
	
	
	/**
	 * Returns the student at specific index.
	 * 
	 * @param 	index	Index for which you want to retrieve a student.
	 * @return			Returns the student at specific index.
	 */
	public Student getStudent(int index);
	
	/**
	 * Returns the index of the specific student.
	 * <code>-1</code> is returned if no student is found.
	 * 
	 * @param student	Student you want to search for.
	 * @return			Returns the index of the specific student.
	 */
	public int indexOf(Student student);
	
	/**
	 * Removes the student at specific index.
	 * 
	 * @param index	Index from which you want to remove a student.
	 */
	public void remove(int index);
	
	/**
	 * Adds a new student to the database with provided parameters.
	 * 
	 * @param studentID		Student ID number.
	 * @param lastName		Last name of the student.
	 * @param firstName		First name of the student.
	 * @return				<code>true</code> if student was successfully
	 * 						added, <code>false</code> otherwise (student
	 * 						already exists in the database).
	 */
	public boolean addStudent(String studentID, String lastName, String firstName);
	
	/**
	 * Adds a listener for the database modifications.
	 * 
	 * @param listener
	 */
	public void addListener(StudentDBModificationListener listener);
	
	/**
	 * Removes a listener for the database modifications at
	 * specific index.
	 * 
	 * @param index
	 */
	public void removeListener(int index);
	
}
