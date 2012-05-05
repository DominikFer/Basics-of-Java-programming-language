package hr.fer.zemris.java.tecaj_8.fraktali;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FraktalParalelno4 {

	public static void main(String[] args) {
		FractalViewer.show(new MojProducer());
	}

	public static class PosaoIzracuna extends RecursiveAction {
		private static final long serialVersionUID = -4316342674508158382L;
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
		static final int treshold = 16;
		
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
		protected void compute() {
			if(yMax-yMin+1 <= treshold) {
				computeDirect();
				return;
			}
			
			invokeAll(
				new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMin+(yMax-yMin)/2, m, data),
				new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin+(yMax-yMin)/2+1, yMax, m, data)
			);
		}
		
		protected void computeDirect() {
			System.out.println("Računam od " + yMin + " do " + yMax + ".");
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
			final int brojTraka = 16;
			
			ForkJoinPool pool = new ForkJoinPool();
			pool.invoke(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, 0, height-1, m, data));
			pool.shutdown();
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
			observer.acceptResult(data, (short) m, requestNo);
		}
		
	}
}
