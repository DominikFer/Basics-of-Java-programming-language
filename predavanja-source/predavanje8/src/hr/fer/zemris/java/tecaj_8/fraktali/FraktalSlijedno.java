package hr.fer.zemris.java.tecaj_8.fraktali;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

public class FraktalSlijedno {

	public static void main(String[] args) {
		FractalViewer.show(new MojProducer());
	}

	public static class MojProducer implements IFractalProducer {

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {
			
			System.out.println("Započinjem izračun...");
			
			int m = 16*16*16;
			int offset = 0;
			short[] data = new short[width*height];
			
			for(int y = 0; y < height; y++) {
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
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
			observer.acceptResult(data, (short) m, requestNo);
		}
		
	}
}
