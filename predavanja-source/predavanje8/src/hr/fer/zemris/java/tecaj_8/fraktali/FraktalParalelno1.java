package hr.fer.zemris.java.tecaj_8.fraktali;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

public class FraktalParalelno1 {

	public static void main(String[] args) {
		FractalViewer.show(new MojProducer());
	}

	public static class PosaoIzracuna implements Runnable {
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
		public void run() {
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
		}
	}
	
	public static class MojProducer implements IFractalProducer {

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {
			
			System.out.println("Započinjem izračun...");
			
			int m = 16*16*16;
			short[] data = new short[width*height];
			
			Thread[] radnici = new Thread[2];
			for(int i = 0; i < radnici.length; i++) {
				int yMin = i == 0 ? 0 : height/2-1;
				int yMax = i == 0 ? height/2 : height-1;
				radnici[i] = new Thread(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data));
			}
			
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {}
				}
			}
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
			observer.acceptResult(data, (short) m, requestNo);
		}
		
	}
}
