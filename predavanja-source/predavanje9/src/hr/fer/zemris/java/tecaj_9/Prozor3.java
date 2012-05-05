package hr.fer.zemris.java.tecaj_9;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor3 extends JFrame {
	
	private static final long serialVersionUID = -2403547639524513786L;

	private int brojac = 0;
	private Posao dretvaRadnika;
	private JLabel labela;
	
	private static class Posao extends Thread {
		volatile boolean stopMe = false;
		private JLabel labela;
		private String[] tekstovi = new String[] {
				"Ovo je prva poruka",
				"Ovo je druga poruka",
				"Ovo je treca poruka"
		};
		private int indexPoruke;
		
		public Posao(JLabel labela) {
			this.labela = labela;
		}
		
		@Override
		public void run() {
			while(!stopMe) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						labela.setText(tekstovi[indexPoruke]);
					}
				});
				
				try { Thread.sleep(1000); } catch (InterruptedException ignorable) {}
				indexPoruke = (indexPoruke+1) % tekstovi.length;
			}
		}
	}
	
	public Prozor3() {
		setLocation(20, 20);
		//setSize(500, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
		
		pack();
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				if(dretvaRadnika == null) {
					dretvaRadnika = new Posao(labela);
					dretvaRadnika.start();
				}
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				if(dretvaRadnika != null) {
					dretvaRadnika.stopMe = true;
					dretvaRadnika = null;
				}
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				
			}
		});
	}
	
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		labela = new JLabel("Pozdrav!!!");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		
		getContentPane().add(labela, BorderLayout.PAGE_START);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton gumb1 = new JButton("Stisni me 1");
		JButton gumb2 = new JButton("Stisni me 2");
		JButton gumb3 = new JButton("Stisni me 3");
		
		gumb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brojac++;
				labela.setText("Gumb je bio pritisnut brojac " + brojac + " puta.");
				System.out.println("Dretva koja ovo izvodi je " + Thread.currentThread().getName());
			}
		});
		
		panel.add(gumb1);
		panel.add(gumb2);
		panel.add(gumb3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Prozor3().setVisible(true);
			}
		});
	}
}
