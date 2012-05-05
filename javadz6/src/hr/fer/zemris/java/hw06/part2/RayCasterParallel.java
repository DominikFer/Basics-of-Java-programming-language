package hr.fer.zemris.java.hw06.part2;

import hr.fer.zemris.java.tecaj_06.rays.GraphicalObject;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.LightSource;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayIntersection;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Program which uses ray-casting to create image of two 3D sphere models under illumination.
 * 
 * @author Sven Kapuđija
 */
public class RayCasterParallel {

	/**
	 * Main method, runs the actual calculation and drawing.
	 * 
	 * @param args	Main arguments (ignored).
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10,0,0), new Point3D(0,0,0), new Point3D(0,0,10), 20, 20);
	}
	
	/**
	 * Producer used to calculate necessary image data (parallelized).
	 * 
	 * @return	Producer with calculation results ready for drawing.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width, int height, long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];
				
				Point3D eyeView = view.sub(eye).modifyNormalize();
				
				//Point3D zAxis = null; // not neccessary
				Point3D yAxis = viewUp.normalize().sub(eyeView.scalarMultiply(viewUp.normalize().scalarProduct(eyeView))).normalize();
				Point3D xAxis = eyeView.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal/2.0)).add(yAxis.scalarMultiply(vertical/2.0));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new ImageCalculation(scene, 0, height, width, height, horizontal, vertical, screenCorner, xAxis, yAxis, eye, red, blue, green));
				pool.shutdown();
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
    }
	
	/**
	 *	Calculates image data in parallel using recursion.
	 */
	private static class ImageCalculation extends RecursiveAction {
		private static final long serialVersionUID = -3602561150015155100L;
		
		private Scene scene;
		private int yMin;
		private int yMax;
		private int width;
		private int height;
		private double horizontal;
		private double vertical;
		private Point3D screenCorner;
		private Point3D xAxis;
		private Point3D yAxis;
		private Point3D eye;
		
		private short[] red;
		private short[] blue;
		private short[] green;
		
		private static final int THRESHOLD = 16;
		
		/**
		 * Creates new calculation class with provided arguments.
		 * 
		 * @param scene				Scene of the image.
		 * @param yMin				Minimum Y-axis value.
		 * @param yMax				Maximum Y-axis value.
		 * @param width				Width of the calculation space/strip.
		 * @param height			Height of the calculation space/strip.
		 * @param horizontal		Horizontal width of the observed space.
		 * @param vertical			Vertical width of the observed space.
		 * @param screenCorner		Screen point of the screen (upper left corner).
		 * @param xAxis				Vector of X-axis.
		 * @param yAxis				Vector of Y-axis.
		 * @param eye				Spectators eye position.
		 * @param red				RED color data.
		 * @param blue				BLUE color data.
		 * @param green				GREEN color data.
		 */
		public ImageCalculation(Scene scene, int yMin, int yMax, int width,
				int height, double horizontal, double vertical, Point3D screenCorner,
				Point3D xAxis, Point3D yAxis, Point3D eye, short[] red,
				short[] blue, short[] green) {
			this.scene = scene;
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.eye = eye;
			this.red = red;
			this.blue = blue;
			this.green = green;
		}

		@Override
		protected void compute() {
			if(yMax-yMin+1 <= THRESHOLD) {
				computeDirect();
				return;
			}
			
			invokeAll(
				new ImageCalculation(scene, yMin, yMin+(yMax-yMin)/2, width, height, horizontal, vertical, screenCorner, xAxis, yAxis, eye, red, blue, green),
				new ImageCalculation(scene, yMin+(yMax-yMin)/2, yMax, width, height, horizontal, vertical, screenCorner, xAxis, yAxis, eye, red, blue, green)
			);
		}
		
		/**
		 * Directly compute if the strip (calculation space) is small enough.
		 */
		protected void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin*width;
			for(int y = yMin; y < yMax; y++) {
				for(int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x/(width-1.0)*horizontal)).sub(yAxis.scalarMultiply(y/(height-1.0)*vertical));
					
					Ray ray = Ray.fromPoints(eye, screenPoint);
					
					tracer(scene, ray, rgb);
					
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					
					offset++;
				}
			}
		}
	}
    
	/**
	 * Traces image using ray-casting model and fills results into <code>rgb</code> array.
	 * 
	 * @param scene	Scene of the image.
	 * @param ray	Ray from the eye (viewer) to the screen point (origin).
	 * @param rgb	RGB color data.
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {
		double[] rgbCalculated = new double[3];
		
		// Ambient light
		rgbCalculated[0] = 15;
		rgbCalculated[1] = 15;
		rgbCalculated[2] = 15;
		
		RayIntersection closestIntersection = findClosestIntersection(scene, ray);
		if (closestIntersection != null) {
			determineColorFor(scene, ray, rgbCalculated, closestIntersection);
		}
		
		rgb[0] = (short) rgbCalculated[0];
		rgb[1] = (short) rgbCalculated[1];
		rgb[2] = (short) rgbCalculated[2];
	}
    
    /**
     * Finds closest intersection between <code>ray</code> and any object in the scene.
     * 
     * @param scene		Scene of the image.
     * @param ray		Ray used to find intersection.
     * @return			Closest intersection with any object in the scene, <code>null</code> if it doesn't exist.
     */
    private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection closestIntersection = null;
		
		List<GraphicalObject> objects = scene.getObjects();
		for(GraphicalObject object : objects) {
			RayIntersection currentIntersection = object.findClosestRayIntersection(ray);
			
			if (currentIntersection != null && (closestIntersection == null  || currentIntersection.getDistance() < closestIntersection.getDistance())) {
				closestIntersection = currentIntersection;
			}
		}
		
		return closestIntersection;
	}
    
    /**
     * Determines color for specific intersection. Adds diffuse and reflective component if necessary.
     * 
     * @param scene					Scene of the image.
     * @param ray					Ray used to find intersection.
     * @param rgbCalculated			RGB color data (which will be filled by results).
     * @param intersection			Intersection between ray and some object (if it exists).
     */
	private static void determineColorFor(Scene scene, Ray ray, double[] rgbCalculated, RayIntersection intersection) {
		List<LightSource> lights = scene.getLights();
		for (LightSource light : lights) {
			Ray rayFromLightToIntersection = Ray.fromPoints(light.getPoint(), intersection.getPoint());
			RayIntersection closestIntersection = findClosestIntersection(scene, rayFromLightToIntersection);
			
			if (closestIntersection != null) {
				double lightSourceIntersectionDistance = light.getPoint().sub(closestIntersection.getPoint()).norm();
				double eyeIntersectionDistance = light.getPoint().sub(intersection.getPoint()).norm();
				
				if (Double.compare(lightSourceIntersectionDistance + 0.01, eyeIntersectionDistance) >= 0) {
					addDiffusseComponent(light, rgbCalculated, closestIntersection);
					addReflectiveComponent(light, ray, rgbCalculated, closestIntersection);
				}
			}
		}
	}
    
	/**
	 * Adds diffuse component to RGB color data.
	 * 
	 * @param light				Light source to provide color data.
	 * @param rgbCalculated		RGB color data (which will be filled by results).
	 * @param intersection		Intersection to focus on.
	 */
	private static void addDiffusseComponent(LightSource light, double[] rgbCalculated, RayIntersection intersection) {
		Point3D n = intersection.getNormal();
		Point3D l = light.getPoint().sub(intersection.getPoint()).normalize();
		
		double ln = l.scalarProduct(n);
		
		rgbCalculated[0] += light.getR()*intersection.getKdr()*Math.max(ln, 0);
		rgbCalculated[1] += light.getG()*intersection.getKdg()*Math.max(ln, 0);
		rgbCalculated[2] += light.getB()*intersection.getKdb()*Math.max(ln, 0);
	}
	
	/**
	 * Adds diffuse component to RGB color data.
	 * 
	 * @param light				Light source to provide color data.
	 * @param ray				Ray used to find intersection.
	 * @param rgbCalculated		RGB color data (which will be filled by results).
	 * @param intersection		Intersection to focus on.
	 */
    private static void addReflectiveComponent(LightSource light, Ray ray, double[] rgbCalculated, RayIntersection intersection) {
		Point3D n = intersection.getNormal();
		Point3D l = light.getPoint().sub(intersection.getPoint());
		Point3D lProjectionOnN = n.scalarMultiply(l.scalarProduct(n));
		Point3D r = lProjectionOnN.add(lProjectionOnN.negate().add(l).scalarMultiply(-1));
		Point3D v = ray.start.sub(intersection.getPoint());
		double cos = r.normalize().scalarProduct(v.normalize());
	    
		if(Double.compare(cos, 0) >= 0) {
	    	cos = Math.pow(cos, intersection.getKrn());
		    
			rgbCalculated[0] += light.getR()*intersection.getKrr()*cos;
			rgbCalculated[1] += light.getG()*intersection.getKrg()*cos;
			rgbCalculated[2] += light.getB()*intersection.getKrb()*cos;
	    }
	}
}