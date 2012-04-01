package hr.fer.zemris.java.graphics.demo;

import hr.fer.zemris.java.graphics.raster.BWRaster;
import hr.fer.zemris.java.graphics.raster.BWRasterMem;
import hr.fer.zemris.java.graphics.shapes.Circle;
import hr.fer.zemris.java.graphics.shapes.Ellipse;
import hr.fer.zemris.java.graphics.shapes.GeometricShape;
import hr.fer.zemris.java.graphics.shapes.Rectangle;
import hr.fer.zemris.java.graphics.shapes.Square;
import hr.fer.zemris.java.graphics.shapes.Triangle;
import hr.fer.zemris.java.graphics.views.RasterView;
import hr.fer.zemris.java.graphics.views.RasterViewException;
import hr.fer.zemris.java.graphics.views.SimpleRasterView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for demonstrating purposes of the {@link SimpleRasterView}, {@link BWRasterMem} and {@link GeometricShape} implementations.
 */
public class Demo {

	/** Raster used for drawing shapes. */
	private static BWRaster raster;
	/** Array of {@link GeometricShape} or other commands. */
	private static GeometricShape[] shapes;
	
	public static void main(String[] args) {
		try {
			createRaster(args);
			
			Scanner sc = new Scanner(System.in);
			
			int shapeCounter = 0;
			try {
				shapeCounter = sc.nextInt();
			} catch (InputMismatchException e) {
				throw new RasterViewException("Invalid parameter type. First parameter, number of shapes, should be an integer.");
			}
			sc.nextLine(); // Escape end-of-line
			shapes = new GeometricShape[shapeCounter];
			
			for(int i = 0; i < shapeCounter; i++) {
				String line = null;
				while((line = sc.nextLine()).isEmpty());
				
				parseAndCreateShape(line, i);
			}
			
			drawShapes();
			
			RasterView view = new SimpleRasterView();
			view.produce(raster);
		} catch (RasterViewException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: Program could not be executed.");
		}
	}
	
	/**
	 * Method for creating raster with user specific width and height.
	 * 
	 * @param args Dimensions of the raster (one or two arguments).
	 * @throws RasterViewException if there is an invalid number of arguments or they aren't integers. 
	 */
	private static void createRaster(String[] args) throws RasterViewException {
		if(args.length < 1 || args.length > 2) {
			throw new RasterViewException("Invalid number of arguments. There should be 1 or 2 arguments.");
		}
		
		if(args.length == 1) {
			if(isInteger(args[0])) {
				raster = new BWRasterMem(Integer.parseInt(args[0]), Integer.parseInt(args[0]));
			} else {
				throw new RasterViewException("Invalid argument type. First argument must be an integer.");
			}
		} else {
			if(isInteger(args[0]) && isInteger(args[1])) {
				raster = new BWRasterMem(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			} else {
				throw new RasterViewException("Invalid argument type. Second argument must be an integer.");
			}
		}
	}
	
	/**
	 * Method for parsing user commands (lines) and creating appropriate shape objects and inserting them into global <code>shapes</code> array.
	 * 
	 * @param line User input which should be parsed.
	 * @param i	Index of <code>shapes</code> array where the shape reference will be put.
	 * @throws RasterViewException if user entered unknown command or the parameter count is incorrect 
	 */
	private static void parseAndCreateShape(String line, int i) throws RasterViewException {
		String[] lineArray = line.split(" ");
		String type = getType(lineArray);
		int params[] = getParams(lineArray);
		
		if(type.toUpperCase().equals("FLIP")) {
			shapes[i] = null;
		} else if (type.toUpperCase().equals("RECTANGLE")) {
			if(params.length < 4)
				throw new RasterViewException("RECTANGLE accepts 4 parameters.");
			shapes[i] = new Rectangle(params[0], params[1], params[2], params[3]);
		} else if (type.toUpperCase().equals("SQUARE")) {
			if(params.length < 3)
				throw new RasterViewException("SQUARE accepts 3 parameters.");
			shapes[i] = new Square(params[0], params[1], params[2]);
		} else if (type.toUpperCase().equals("CIRCLE")) {
			if(params.length < 3)
				throw new RasterViewException("CIRCLE accepts 3 parameters.");
			shapes[i] = new Circle(params[0], params[1], params[2]);
		} else if (type.toUpperCase().equals("ELLIPSE")) {
			if(params.length < 4)
				throw new RasterViewException("ELLIPSE accepts 4 parameters.");
			shapes[i] = new Ellipse(params[0], params[1], params[2], params[3]);
		} else if (type.toUpperCase().equals("TRIANGLE")) {
			if(params.length < 6)
				throw new RasterViewException("TRIANGLE accepts 6 parameters.");
			shapes[i] = new Triangle(params[0], params[1], params[2], params[3], params[4], params[5]);
		} else {
			throw new RasterViewException("Unkown command '" + type + "'. Allowed commands: FLIP, RECTANGLE, SQUARE, CIRCLE, ELLIPSE, TRIANGLE.");
		}
	}
	
	/**
	 * Method for drawing shapes on the raster or enabling/disabling raster flip mode.
	 */
	private static void drawShapes() {
		boolean flipMode = false;
		for(GeometricShape shape : shapes) {
			if(shape == null) {
				if(flipMode) {
					raster.disableFlipMode();
					flipMode = false;
				} else {
					raster.enableFlipMode();
					flipMode = true;
				}
			} else {
				shape.draw(raster);
			}
		}
	}
	
	/**
	 * Method for extracting (integer) parameters from user input.
	 * 
	 * @param lineArray User line with command and parameters.
	 * @return Integer array filled with user parameters.
	 * @throws RasterViewException if any of the parameter is not an integer.
	 */
	private static int[] getParams(String[] lineArray) throws RasterViewException {
		int[] params = new int[lineArray.length-1];
		
		for(int i = 1; i < lineArray.length; i++) {
			String s = lineArray[i];
			if(isInteger(s)) {
				params[i-1] = Integer.parseInt(s);
			} else {
				throw new RasterViewException("All shape parameters should be integers.");
			}
		}
		
		return params;
	}
	
	/**
	 * Method for extracting command (shape type) from user input.
	 * 
	 * @param lineArray User line with command and parameters.
	 * @return Type of the command (shape).
	 * @throws RasterViewException if the command is not recognized.
	 */
	private static String getType(String[] lineArray) throws RasterViewException {
		if(lineArray.length < 1) {
			throw new RasterViewException("Unknown command. Command should be separated by space from the parameters.");
		}
		
		return lineArray[0];
	}
	
	
	/**
	 * Tests if provided <code>String</code> is <code>int</code>.
	 * 
	 * @param s String you want to parse into <code>int</code.
	 * @return <code>true</code> if string is <code>int</code>, <code>false</code> otherwise
	 */
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}