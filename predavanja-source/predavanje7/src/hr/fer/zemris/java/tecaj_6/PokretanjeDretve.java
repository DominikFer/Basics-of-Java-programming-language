package hr.fer.zemris.java.tecaj_6;

public class PokretanjeDretve {

	private static volatile long brojac = 0;
	
	public static void main(String[] args) {
		PosaoDretve p = new PosaoDretve();
		
		Thread t = new Thread(p);
		t.start();
		
		while(true) {
			try {
				t.join();
				break;
			} catch (InterruptedException ignorable) {
				
			}
		}
		
		System.out.println("Konacno stanje brojaca je " + brojac);
	}

	public static class PosaoDretve implements Runnable {

		@Override
		public void run() {
			for(int i = 0; i < 10_000_000; i++) {
				brojac += 1;
			}
		}

	}
}
