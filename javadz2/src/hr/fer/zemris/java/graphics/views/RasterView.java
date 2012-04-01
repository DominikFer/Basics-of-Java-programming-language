package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 *	Interface which defines method for displaying/printing raster on the screen. 
 */
public interface RasterView {

	/**
	 * Displays (prints) raster on the screen with predefined characters. 
	 * 
	 * @param raster Raster from which you want to print to the {@link RasterView}.
	 */
	public void produce(BWRaster raster);
}
