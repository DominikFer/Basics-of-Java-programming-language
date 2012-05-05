package hr.fer.zemris.java.tecaj_9;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class OtvaranjeProzora {

	public static void main(String[] args) {
		izdampajDretve("Stanje prije stvaranja prozora:");
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				
				frame.setLocation(20, 20);
				frame.setSize(500, 200);
				
				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				frame.setVisible(true);
			}
		});
		
		izdampajDretve("Stanje nakon stvaranja prozora:");
	}
	
	private static void izdampajDretve(String poruka) {
		System.out.println(poruka);
		System.out.println("==============================");
		
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		while(tg.getParent() != null) {
			tg = tg.getParent();
		}
		tg.list();
		System.out.println();
	}

}
