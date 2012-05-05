package hr.fer.zemris.java.tecaj_9;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor1 extends JFrame {
	
	private static final long serialVersionUID = -2403547639524513786L;

	public Prozor1() {
		setLocation(20, 20);
		setSize(500, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Prozor1().setVisible(true);
			}
		});
	}
}
