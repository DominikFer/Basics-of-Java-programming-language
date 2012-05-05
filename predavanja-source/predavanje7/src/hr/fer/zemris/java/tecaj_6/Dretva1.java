package hr.fer.zemris.java.tecaj_6;

public class Dretva1 extends Thread {

	@Override
	public void run() {
		System.out.println("Pozdravi iz dretve koja se zove: " + Thread.currentThread().getName());
		
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		while(tg.getParent() != null) {
			tg = tg.getParent();
		}
		
		tg.list();
	}
}
