package hr.fer.zemris.java.hw06.part1;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Program for calculation and display of Newton-Raphson iteration-based fractal.
 * 
 * @author Sven Kapuđija
 *
 */
public class Newton {

	/** Maximum iterations for Newton-Rapshon computation **/
	private static final int 	MAX_ITERATIONS = Integer.MAX_VALUE;
	/** Convergence threshold for Newton-Rapshon computation **/
	private static final double CONVERGENCE_THRESHOLD = 0.002;
	
	private static ComplexRootedPolynomial rootedPolynom;
	private static ComplexPolynomial derivedPolynom;
	
	/**
	 * Newton-Raphson iteration-based fractal viewer.
	 * 
	 * @param args	Main arguments (ignored).
	 */
	public static void main(String[] args) {
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		List<Complex> roots = new ArrayList<Complex>();
		
		// Get the roots from the keyboard
        Scanner sc = new Scanner(System.in);
        while(true) {
        	System.out.print("Root " + (roots.size() + 1) + "> ");
        	String currentLine = sc.nextLine();
        	if(currentLine.equals("done"))
        		break;
        	Complex c = Complex.parseString(currentLine);
        	if(c != null)
        		roots.add(c);
        }
        
        System.out.println("Image of fractal will appear shortly. Thank you.");
        
        rootedPolynom = new ComplexRootedPolynomial(roots.toArray(new Complex[roots.size()]));
        derivedPolynom = rootedPolynom.toComplexPolynom().derive();
        
        FractalViewer.show(new NewtonRapshonProducer());
	}

	/**
	 * Class which computes variables necessary for drawing fractal.
	 */
	private static class NewtonRapshonComputation implements Callable<Void> {
		
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		
		short[] data;
		
		/**
		 * Creates new computation class with provided arguments.
		 * 
		 * @param reMin		Minimum real value.
		 * @param reMax		Maximum real value.
		 * @param imMin		Minimum imaginary value.
		 * @param imMax		Maximum imaginary value.
		 * @param width		Width of the drawing window.
		 * @param height	Height of the drawing window.
		 * @param yMin		Minimum value of Y-axis.
		 * @param yMax		Maximum value of Y-axis.
		 * @param data		Datastore for calculation results.
		 */
		public NewtonRapshonComputation(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax, short[] data) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
		}

		@Override
		public Void call() throws Exception {
			int offset = yMin*width;
			for(int y = yMin; y <= yMax; y++) {
				for(int x = 0; x < width; x++) {
					double cRe = x/(width-1.0) * (reMax - reMin) + reMin;
					double cIm = (height-1-y) / (height-1.0) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cRe, cIm);
					Complex zn1 = new Complex();
					
					int iterations = 0;
					double module = 0;
					
					do {
						Complex fraction = rootedPolynom.apply(zn).divide(derivedPolynom.apply(zn));
						
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						
						iterations++;
					} while ((iterations < MAX_ITERATIONS) && (module > CONVERGENCE_THRESHOLD));
					
					short index = (short) rootedPolynom.indexOfClosestRootFor(zn1, CONVERGENCE_THRESHOLD);
					data[offset++] = (short) (index == -1 ? 0 : index + 1);
				}
			}
			
			return null;
		}
	}
	
	/**
	 * Producer used to display and calculate fractal with the help of parallelization.
	 */
	private static class NewtonRapshonProducer implements IFractalProducer {

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver observer) {
			int numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors();
			
			ExecutorService pool = Executors.newFixedThreadPool(numberOfAvailableProcessors);
			List<Future<Void>> results = new ArrayList<Future<Void>>();
			
			System.out.println("Započinjem izračun...");
			
			short[] data = new short[width*height];
			int stripsCount = height/8*numberOfAvailableProcessors;
			int stripHeight = height/stripsCount;
			
			for(int i = 0; i < stripsCount; i++) {
				int yMin = i*stripHeight;
				int yMax = (i+1)*stripHeight-1;
				
				if(i == (stripsCount-1)) {
					yMax = height-1;
				}
				
				NewtonRapshonComputation job = new NewtonRapshonComputation(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data);
				results.add(pool.submit(job));
			}
			
			for(Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException ignorable) {}
			}
			pool.shutdown();
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
			observer.acceptResult(data, (short) (rootedPolynom.order() + 1), requestNo);
		}
	}
}
