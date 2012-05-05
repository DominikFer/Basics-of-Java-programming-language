package hr.fer.zemris.java.tecaj_9;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor2 extends JFrame {
	
	private static final long serialVersionUID = -2403547639524513786L;

	public Prozor2() {
		setLocation(20, 20);
		setSize(500, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}
	
	private void initGUI() {
		JLabel labela = new JLabel("Pozdrav!!!");
		JButton gumb = new JButton("Stisni me");
		
		labela.setBounds(10, 10, 300, 20);
		
		gumb.setLocation(10, 50);
		gumb.setSize(100, 20);
		
		getContentPane().add(labela);
		getContentPane().add(gumb);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Prozor2().setVisible(true);
			}
		});
	}
}
