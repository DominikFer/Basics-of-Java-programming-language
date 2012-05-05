package hr.fer.zemris.java.tecaj_8.primjer1;

import java.util.LinkedList;

public class MyBlockingQueue {
	
	private LinkedList<Runnable> lista = new LinkedList<>();
	private int kapacitet;
	
	public MyBlockingQueue(int kapacitet) {
		if(kapacitet < 1) {
			throw new IllegalArgumentException("Kapacitet ne može biti manji od 1 (predano je " + kapacitet + ").");
		}
		this.kapacitet = kapacitet;
	}
	
	public synchronized void addJob(Runnable job) {
		while(lista.size() >= this.kapacitet) {
			try {
				System.out.println("Dretva " + Thread.currentThread().getName() + " ide na spavanje jer ne može postaviti posao: red je pun!");
				this.wait();
				System.out.println("Dretva " + Thread.currentThread().getName() + " je probuđena; ide provjeriti može li dodati posao.");
			} catch (InterruptedException ignorable) {}
		}
		System.out.println("Dretva " + Thread.currentThread().getName() + " postavlja novi posao u red.");
		lista.add(job);
		this.notifyAll();
	}
	
	public synchronized Runnable takeJob() {
		while(lista.isEmpty()) {
			try {
				System.out.println("Dretva " + Thread.currentThread().getName() + " ide na spavanje jer ne može uzeti posao: red je prazan!");
				this.wait();
				System.out.println("Dretva " + Thread.currentThread().getName() + " je probuđena; ide provjeriti može li uzeti posao.");
			} catch (InterruptedException ignorable) {}
		}
		System.out.println("Dretva " + Thread.currentThread().getName() + " uzima posao iz reda.");
		Runnable r = lista.pop();
		this.notifyAll();
		return r;
	}
}
