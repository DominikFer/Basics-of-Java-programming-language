package hr.fer.zemris.java.hw07.students;

import javax.swing.SwingUtilities;

/**
 * Program which display UI for manipulating student database
 * (view, create, delete).
 */
public class StudentBrowser {

	/**
	 * Main method.
	 * 
	 * @param args	Console-line arguments (ignored).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new StudentGUI().setVisible(true);
			}
		});
	}

}
