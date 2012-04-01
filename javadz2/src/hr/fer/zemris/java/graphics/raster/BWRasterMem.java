package hr.fer.zemris.java.graphics.raster;

import java.util.BitSet;

public class BWRasterMem implements BWRaster {

	private BitSet raster;
	private int width;
	private int height;
	/** Flip mode is default <code>FALSE</code>. When activated <code>turnOn</code>
	 * method flips the state of the pixel instead setting it <code>on</code>. */
	private boolean flipMode = false;
	
	/**
	 * Raster with in-memory storage.
	 * 
	 * @param width Raster width.
	 * @param height Raster height.
	 */
	public BWRasterMem(int width, int height) {
		this.width = width;
		this.height = height;
		this.raster = new BitSet(width*height);
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public void clear() {
		this.raster.clear();
	}

	@Override
	public void turnOn(int x, int y) {
		if(this.flipMode) {
			this.raster.flip(getPixelIndex(x, y));
		} else {
			this.raster.set(getPixelIndex(x, y));
		}
	}

	@Override
	public void turnOff(int x, int y) {
		this.raster.clear(getPixelIndex(x, y));
	}

	@Override
	public void enableFlipMode() {
		this.flipMode = true;
	}

	@Override
	public void disableFlipMode() {
		this.flipMode = false;
	}

	@Override
	public boolean isTurnedOn(int x, int y) {
		return this.raster.get(getPixelIndex(x, y));
	}

	/**
	 * Method for converting X and Y coordinates into array index.
	 * 
	 * @param x X-coordinate of the specific pixel.
	 * @param y Y-coordinate of the specific pixel.
	 * @return Index of the array of specific pixel.
	 */
	private int getPixelIndex(int x, int y) {
		return y * width + x;
	}
}
