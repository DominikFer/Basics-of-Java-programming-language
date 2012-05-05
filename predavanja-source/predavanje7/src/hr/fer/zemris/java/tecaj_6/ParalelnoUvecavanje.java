package hr.fer.zemris.java.tecaj_6;


public class ParalelnoUvecavanje {

	private static long brojac;
	
	public static void main(String[] args) {
		brojac = 0;
		
		PosaoDretve p = new PosaoDretve();
		
		Thread[] dretve = new Thread[5];
		for(int i = 0; i < dretve.length; i++) {
			dretve[i] = new Thread(p);
		}
		for(int i = 0; i < dretve.length; i++) {
			dretve[i].start();
		}
		for(int i = 0; i < dretve.length; i++) {
			while(true) {
				try {
					dretve[i].join();
					break;
				} catch(InterruptedException ignorable) {}
			}
		}
		
		synchronized (p) {
			System.out.println("Konacno stanje brojaca je " + brojac);
		}
	}

	public static class PosaoDretve implements Runnable {

		@Override
		public synchronized void run() {
			for(int i = 0; i < 10_000_000; i++) {
				brojac += 1;
			}
		}

	}
}
