package hr.fer.zemris.java.hw07.students;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * File representation of the {@link StudentDatabase}. 
 */
public class FileStudentDatabase implements StudentDatabase {

	private List<StudentDBModificationListener> listeners = new ArrayList<StudentDBModificationListener>();
	
	private List<Student> students = new ArrayList<Student>();
	private Path studentsFile;
	
	/**
	 * Constructor. Reads all the students from the file. If the file
	 * does not exist, it's created.
	 * 
	 * @param fileName
	 */
	public FileStudentDatabase(String fileName) {
		this.studentsFile = Paths.get(fileName);
		
		if(!studentsFile.toFile().exists()) {
			System.out.println("File '" + fileName + "' does not exist. Creating new one...");
			
			File outputFile = new File(studentsFile.getFileName().toString());
			Path outputPath = outputFile.toPath();
			if(!outputFile.exists()) {
				try {
					Files.createFile(outputPath);
				} catch (IOException ignore) {}
			}
		}
		
		readAllRecords();
	}
	
	/**
	 * Reads all the records from the file. 
	 */
	private void readAllRecords() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(studentsFile.getFileName().toString())), "UTF-8"));
			String line = br.readLine();
			while(line != null) {
				String[] lineSplitted = line.split("\\t");
				if(lineSplitted.length != 3) continue;
				
				students.add(new Student(lineSplitted[0], lineSplitted[1], lineSplitted[2]));

				line = br.readLine();
			}
			br.close();
			
			notifyListenersFileRead();
		} catch (UnsupportedEncodingException ignorable) {
			System.out.println("Unsupported encoding UTF-8.");
		} catch (FileNotFoundException ignorable) {
			System.out.println("File not found. Trying to create a new one...");
			
			File outputFile = new File(studentsFile.getFileName().toString());
			Path outputPath = outputFile.toPath();
			if(!outputFile.exists()) {
				try {
					Files.createFile(outputPath);
				} catch (IOException ignore) {}
			}
			
			readAllRecords();
		} catch (IOException ignorable) {
			System.out.println("Input/output exception.");
		}
	}
	
	@Override
	public int size() {
		return students.size();
	}

	@Override
	public Student getStudent(int index) {
		if(index < students.size()) {
			return students.get(index);
		} else {
			throw new IllegalArgumentException("Index " + index + " is out of range!");
		}
	}

	@Override
	public int indexOf(Student student) {
		return students.indexOf(student);
	}

	@Override
	public void remove(int index) {
		if(index < students.size()) {
			students.remove(index);
			
			clearFile();
			if(students.size() > 0)
				writeToFile(students.get(0).getStudentID(), students.get(0).getLastName(), students.get(0).getFirstName(), false);
			
			for(int i = 1; i < students.size(); i++) {
				writeToFile(students.get(i).getStudentID(), students.get(i).getLastName(), students.get(i).getFirstName(), true);
			}
			
			notifyListenersStudentRemoved(index);
		} else {
			throw new IllegalArgumentException("Index " + index + " is out of range!");
		}
	}
	
	@Override
	public boolean addStudent(String studentID, String lastName, String firstName) {
		if(indexOf(new Student(studentID, lastName, firstName)) != -1)
			return false;
		
		boolean writeNewLineBefore = true;
		if(students.size() == 0) {
			writeNewLineBefore = false;
		}
		
		boolean writeSuccessfull = writeToFile(studentID, lastName, firstName, writeNewLineBefore);
		if(writeSuccessfull) {
			students.add(new Student(studentID, lastName, firstName));
			notifyListenersStudentAdded(students.size()-1);
		}
			
		return writeSuccessfull;
	}
	
	/**
	 * Writes a new student in the file.
	 * 
	 * @param studentID				Student ID number.
	 * @param lastName				Last name of the student.
	 * @param firstName				First name of the student.
	 * @param writeNewLineBefore	<code>false</code> if you are writing at the
	 *  							beginning of the file, <code>true</code> otherwise.
	 * @return						<code>true</code> if writing was successful,
	 * 								<code>false</code> otherwise.
	 */	
	private boolean writeToFile(String studentID, String lastName, String firstName, boolean writeNewLineBefore) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(studentsFile.getFileName().toString(), true)),"UTF-8"));
			if(writeNewLineBefore)
				bw.newLine();
			bw.write(studentID + "\t" + lastName + "\t" + firstName);
			bw.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported encoding UTF-8.");
			return false;
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			return false;
		} catch (IOException e) {
			System.out.println("Input/output exception.");
			return false;
		}
				
		return true;
	}
	
	/**
	 * Clears the database file (removes all the records).
	 */
	private void clearFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(studentsFile.getFileName().toString(), false)),"UTF-8"));
			bw.write("");
			bw.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported encoding UTF-8.");
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException e) {
			System.out.println("Input/output exception.");
		}
	}
	
	/**
	 * Notifies all the listeners that the new student is added to the database.
	 * 
	 * @param index		Index at which new student is added.
	 */
	private void notifyListenersStudentAdded(int index) {
		for(StudentDBModificationListener listener : listeners) {
			listener.studentsAdded(this, index, index);
		}
	}

	/**
	 * Notifies all the listeners that the student is removed to the database.
	 * 
	 * @param index		Index at which new student is removed.
	 */
	private void notifyListenersStudentRemoved(int index) {
		for(StudentDBModificationListener listener : listeners) {
			listener.studentsRemoved(this, index, index);
		}
	}
	
	/**
	 * Notifies all the listeners that the database change is to complex so
	 * they should refresh.
	 */
	private void notifyListenersFileRead() {
		for(StudentDBModificationListener listener : listeners) {
			listener.databaseChanged(this);
		}
	}
	
	@Override
	public void addListener(StudentDBModificationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(int index) {
		if(index < listeners.size())
			listeners.remove(index);
	}
}
