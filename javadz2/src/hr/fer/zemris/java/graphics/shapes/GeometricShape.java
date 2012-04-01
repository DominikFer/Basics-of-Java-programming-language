package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public abstract class GeometricShape {
	
	/**
	 * Draw shape on the raster.
	 * 
	 * @param r Raster on which you want to draw.
	 */
	public void draw(BWRaster r) {
		for(int y = 0; y < r.getHeight(); y++) {
			for(int x = 0; x < r.getWidth(); x++) {
				if(this.containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
	
	/**
	 * Test if shape contains specific pixel/point or not.
	 * 
	 * @param x	X-coordinate of the point.
	 * @param y Y-coordinate of the point.
	 * @return <code>TRUE</code> if shape contains that specific point, <code>FALSE</code> otherwise.
	 */
	public abstract boolean containsPoint(int x, int y);
}
