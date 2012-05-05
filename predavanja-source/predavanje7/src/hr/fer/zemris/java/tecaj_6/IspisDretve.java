package hr.fer.zemris.java.tecaj_6;

public class IspisDretve {
	
	public static void main(String[] args) {
		
		Thread t = Thread.currentThread();
		System.out.println("Trenutna dretva se zove: " + t.getName());
		System.out.println("Prioritet trenutne dretve je: " + t.getPriority());
		System.out.println("Trenutna dretva je demon? " + t.isDaemon());
		System.out.println("Trenutna dretva pripada grupi: " + t.getThreadGroup().getName());
		System.out.println();
		
		ThreadGroup tg = t.getThreadGroup();
		while(tg.getParent() != null) {
			tg = tg.getParent();
		}
		
		tg.list();
		
	}
}
