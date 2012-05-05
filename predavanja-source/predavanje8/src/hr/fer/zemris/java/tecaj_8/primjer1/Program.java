package hr.fer.zemris.java.tecaj_8.primjer1;

public class Program {

	public static void main(String[] args) {
		final int brojProizvodaca = 200;
		final int brojPotrosaca = 300;
		final int brojPoslovaPoProizvodacu = 20000;
		final int kapacitetReda = 200000;
		
		final MyBlockingQueue queue = new MyBlockingQueue(kapacitetReda);
		for(int i = 0; i < brojPotrosaca; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						Runnable job = queue.takeJob();
						job.run();
					}
				}
			}, "Potrosac_" + i);
			t.start();
		}

		for(int i = 0; i < brojProizvodaca; i++) {
			final int trenutniProizvodac = i;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int j = 0; j < brojPoslovaPoProizvodacu; j++) {
						Runnable job = new Posao(trenutniProizvodac*brojPoslovaPoProizvodacu+j);
						queue.addJob(job);
					}
				}
			}, "Proizvodac_" + i);
			t.start();
		}
	}

	private static class Posao implements Runnable {
		private int brojPosla;
		
		public Posao(int brojPosla) {
			this.brojPosla = brojPosla;
		}
		
		@Override
		public void run() {
			System.out.println("Radi se neki posao " + brojPosla + "...");
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
			System.out.println("Posao " + brojPosla + " je gotov.");
		}
	}
}
