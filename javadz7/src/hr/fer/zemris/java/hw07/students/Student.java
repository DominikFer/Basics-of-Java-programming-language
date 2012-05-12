package hr.fer.zemris.java.hw07.students;

/**
 * Class which defines single {@link Student} record. 
 */
public class Student {

	private String studentID;
	private String lastName;
	private String firstName;
	
	/**
	 * Creates new student with provided parameters.
	 * 
	 * @param studentID		Student ID number.
	 * @param lastName		Last name of the student.
	 * @param firstName		First name of the student.
	 */
	public Student(String studentID, String lastName, String firstName) {
		this.studentID = studentID;
		this.lastName = lastName;
		this.firstName = firstName;
	}
	
	/**
	 * @return Returns student ID.
	 */
	public String getStudentID() {
		return studentID;
	}
	
	/**
	 * @return Returns student first name.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @return Returns student last name.
	 */
	public String getLastName() {
		return lastName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Student)) return false;
		
		if(!((Student) obj).getStudentID().trim().toLowerCase().equals(this.getStudentID().trim().toLowerCase())) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Student [studentID=" + studentID + ", lastName=" + lastName
				+ ", firstName=" + firstName + "]";
	}

	
}
