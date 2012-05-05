package hr.fer.zemris.java.tecaj_9;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class Prozor4 extends JFrame {
	
	private static final long serialVersionUID = -2403547639524513786L;

	public Prozor4() {
		setLocation(20, 20);
		setSize(500, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}
	
	private void initGUI() {
		final ModelListParnihBrojeva model = new ModelListParnihBrojeva(2);
		final JList<Integer> lista = new JList<Integer>(model);
		
		final MojaLabela labela = new MojaLabela(model);
		final JButton gumb = new JButton("Povecaj");
		gumb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.povecajBrojStavki();
			}
		});
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(labela, BorderLayout.PAGE_START);
		getContentPane().add(new JScrollPane(lista), BorderLayout.CENTER);
		getContentPane().add(gumb, BorderLayout.PAGE_END);
	}

	public static class MojaLabela extends JLabel implements ListDataListener {
		
		public MojaLabela(ListModel<Integer> model) {
			model.addListDataListener(this);
			azurirajPogled(model);
		}

		private void azurirajPogled(ListModel<Integer> model) {
			setText("Broj stavki liste je " + model.getSize());
		}

		@SuppressWarnings("unchecked")
		@Override
		public void contentsChanged(ListDataEvent e) {
			azurirajPogled((ListModel<Integer>) e.getSource());
		}

		@SuppressWarnings("unchecked")
		@Override
		public void intervalAdded(ListDataEvent e) {
			azurirajPogled((ListModel<Integer>) e.getSource());
		}

		@SuppressWarnings("unchecked")
		@Override
		public void intervalRemoved(ListDataEvent e) {
			azurirajPogled((ListModel<Integer>) e.getSource());
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Prozor4().setVisible(true);
			}
		});
	}
	
	private static class ModelListParnihBrojeva implements ListModel<Integer> {

		private int n;
		private List<ListDataListener> slusaci = new ArrayList<>();
		
		public ModelListParnihBrojeva(int n) {
			this.n = n;
		}
		
		@Override
		public Integer getElementAt(int index) {
			return Integer.valueOf(index*2);
		}

		@Override
		public int getSize() {
			return n;
		}
		
		public void povecajBrojStavki() {
			n++;
			obavijestiSlusaceDaJeDodanaStavka(n-1);
		}
		
		private void obavijestiSlusaceDaJeDodanaStavka(int index) {
			ListDataEvent dogadaj = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
			ListDataListener[] polje = slusaci.toArray(new ListDataListener[0]);
			for(ListDataListener l : polje) {
				l.intervalAdded(dogadaj);
			}
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			if(!slusaci.contains(l)) {
				slusaci.add(l);
			}
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			if(slusaci.contains(l)) {
				slusaci.remove(l);
			}
		}
	}
}
