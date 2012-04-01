package hr.fer.zemris.java.graphics.raster;

/**
 * Interface which defines raster manipulation options.
 */
public interface BWRaster {
	
	/**
	 * @return Raster width.
	 */
	public int getWidth();
	/**
	 * @return Raster Height.
	 */
	public int getHeight();
	/**
	 * Clear all the pixels (put them in <code>off</code> state).
	 */
	public void clear();
	/**
	 * Turns on the pixel at specific location. If <code>flipMode</code>
	 * activated, it flips the current pixel state instead of setting it
	 * to <code>on</code> state.
	 * 
	 * @param x X-coordinate of pixel.
	 * @param y Y-coordinate of pixel.
	 */
	public void turnOn(int x, int y);
	/**
	 * Turns off pixel at specific location (set its state to <code>off</code>).
	 * 
	 * @param x X-coordinate of pixel.
	 * @param y Y-coordinate of pixel.
	 */
	public void turnOff(int x, int y);
	/**
	 * Enables flip mode (when <code>turnOn</code> is called, it flips the
	 * state instead of setting it to <code>on</code>).
	 */
	public void enableFlipMode();
	/**
	 * Disables flip mode (<code>turnOn</code> works normally).
	 */
	public void disableFlipMode();
	/**
	 * Checks if pixel on specific location is turned <code>on</code>.
	 * 
	 * @param x X-coordinate of pixel.
	 * @param y Y-coordinate of pixel.
	 * @return <code>TRUE</code> if pixel is turned <code>on</code>, <code>FALSE</code> otherwise.
	 */
	public boolean isTurnedOn(int x, int y);
}
