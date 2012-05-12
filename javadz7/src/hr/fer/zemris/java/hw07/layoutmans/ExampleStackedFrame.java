package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.GridLayout;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * GUI class for the purpose of StackedFrame layout manager demonstration. 
 */
public class ExampleStackedFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, creates new GUI.
	 */
	public ExampleStackedFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Primjer uporabe StackedLayouta");
		initGUI();
		pack();
	}
	
	/**
	 * Initializes GUI with 1x3 Grid Layout (3 panels).
	 */
	private void initGUI() {
		this.getContentPane().setLayout(new GridLayout(1, 3));
		this.getContentPane().add(makePanel("Odozgo", new StackedLayout(StackedLayoutDirection.FROM_TOP)));
		this.getContentPane().add(makePanel("Odozdo", new StackedLayout(StackedLayoutDirection.FROM_BOTTOM)));
		this.getContentPane().add(makePanel("Ispuna", new StackedLayout(StackedLayoutDirection.FILL)));
	}
	
	/**
	 * Creates new {@link JComponent} panel with populated content (panels).
	 * 
	 * @param tekst		Panel title.
	 * @param manager	Layout manager used for this panel.
	 * @return			New instance of {@link JComponent} panel.
	 */
	private JComponent makePanel(String tekst, LayoutManager2 manager) {
		JPanel panel = new JPanel(manager);
		panel.setBorder(BorderFactory.createTitledBorder(tekst));
		
		JPanel p1 = new JPanel(new GridLayout(3, 1));
		p1.setBorder(BorderFactory.createTitledBorder("Komponenta 1"));
		p1.add(new JButton("Gumb 1"));
		p1.add(new JButton("Gumb 2"));
		p1.add(new JButton("Gumb 3"));
		
		JPanel p2 = new JPanel(new GridLayout(2, 1));
		p2.setBorder(BorderFactory.createTitledBorder("Komponenta 2"));
		p2.add(new JLabel("Prva od dvije labele"));
		p2.add(new JLabel("Druga od dvije labele"));
		
		panel.add(p1);
		panel.add(p2);
		panel.add(new JLabel("Izolirana labela"));
		
		return panel;
	}
	
	/**
	 * Main method.
	 * 
	 * @param args	Command-line arguments (ignored).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ExampleStackedFrame().setVisible(true);
			}
		});
	}

}
