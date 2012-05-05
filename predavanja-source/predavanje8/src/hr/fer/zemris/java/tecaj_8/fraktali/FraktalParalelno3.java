package hr.fer.zemris.java.tecaj_8.fraktali;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FraktalParalelno3 {

	public static void main(String[] args) {
		FractalViewer.show(new MojProducer());
	}

	public static class PosaoIzracuna implements Callable<Void> {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;

		public PosaoIzracuna(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax, int m, short[] data) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
		}

		@Override
		public Void call() {
			int offset = yMin*width;
			for(int y = yMin; y <= yMax; y++) {
				for(int x = 0; x < width; x++) {
					double cRe = x/(width-1.0) * (reMax - reMin) + reMin;
					double cIm = (height-1-y) / (height-1.0) * (imMax - imMin) + imMin;
					double zRe = 0;
					double zIm = 0;
					double modul = 0;
					int iters = 0;
					do {
						double zn1Re = zRe*zRe - zIm*zIm + cRe;
						double zn1Im = 2*zRe*zIm + cIm;
						modul = zn1Re*zn1Re + zn1Im*zn1Im;
						zRe = zn1Re;
						zIm = zn1Im;
						iters++;
					} while(iters < m && modul < 4.0);
					
					data[offset] = (short) iters;
					offset++;
				}
			}
			return null;
		}
	}
	
	public static class MojProducer implements IFractalProducer {

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {
			
			System.out.println("Započinjem izračun...");
			
			int m = 16*16*16;
			short[] data = new short[width*height];
			final int brojTraka = 16;
			int brojYPoTraci = height/brojTraka;
			
			ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			List<Future<Void>> rezultati = new ArrayList<>();
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				
				if(i == (brojTraka-1)) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data);
				rezultati.add(pool.submit(posao));
			}
			
			for(Future<Void> posao : rezultati) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {}
			}
			pool.shutdown();
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
			observer.acceptResult(data, (short) m, requestNo);
		}
		
	}
}
