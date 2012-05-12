package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * GUI class for the purpose of SimpleLayout layout manager demonstration. 
 */
public class ExampleSimpleLayoutFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, creates new GUI.
	 */
	public ExampleSimpleLayoutFrame() {
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
		this.getContentPane().add(makePanel("Lijevo", SimpleLayoutPlacement.LEFT, null));
		this.getContentPane().add(makePanel("Centar", SimpleLayoutPlacement.CENTER, null));
		this.getContentPane().add(makePanel("Lijevo i Centar", SimpleLayoutPlacement.LEFT, SimpleLayoutPlacement.CENTER));
	}
	
	/**
	 * Creates new {@link JComponent} panel with populated content (panels).
	 * 
	 * @param text				Panel title.
	 * @param firstDirection	Direction of the first panel.
	 * @param secondDirection	Direction of the second panel.
	 * @return					New instance of {@link JComponent} panel.
	 */
	private JComponent makePanel(String text, SimpleLayoutPlacement firstDirection, SimpleLayoutPlacement secondDirection) {
		JPanel panel = new JPanel(new SimpleLayout());
		panel.setBorder(BorderFactory.createTitledBorder(text));
		
		JPanel p1 = new JPanel(new GridLayout(3, 1));
		p1.setBorder(BorderFactory.createTitledBorder("Komponenta 1"));
		p1.add(new JButton("Gumb 1"));
		p1.add(new JButton("Gumb 2"));
		p1.add(new JButton("Gumb 3"));
		
		new BorderLayout();
		
		JPanel p2 = new JPanel(new GridLayout(2, 1));
		p2.setBorder(BorderFactory.createTitledBorder("Komponenta 2"));
		p2.add(new JLabel("Prva od dvije labele"));
		p2.add(new JLabel("Druga od dvije labele"));
		
		panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		panel.add(p1, firstDirection);
		if(secondDirection != null)
			panel.add(p2, secondDirection);
		
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
				new ExampleSimpleLayoutFrame().setVisible(true);
			}
		});
	}

}
