package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class SimpleRasterView implements RasterView {

	private static final char onChar = '*';
	private static final char offChar = '.';
	
	private char on;
	private char off;
	
	/**
	 * Create new <code>view</code> with user defined
	 * <code>on</code> and <code>off</code> characters.
	 * 
	 * @param on Character which will represent turned <code>on</code> pixel.
	 * @param off Character which will represent turned <code>off</code> pixel.
	 */
	public SimpleRasterView(char on, char off) {
		this.on = on;
		this.off = off;
	}
	
	/**
	 * Create new <code>view</code> with predefined
	 * <code>on</code> and <code>off</code> characters.
	 * 
	 */
	public SimpleRasterView() {
		this(onChar, offChar);
	}

	@Override
	public void produce(BWRaster raster) {
		for(int y = 0; y < raster.getHeight(); y++) {
			for(int x = 0; x < raster.getWidth(); x++) {
				if(raster.isTurnedOn(x, y)) {
					printOnChar();
				} else {
					printOffChar();
				}
			}
			printNewLine();
		}
	}

	private void printNewLine() {
		System.out.println();
	}
	
	private void printOnChar() {
		System.out.print(this.on);
	}
	
	private void printOffChar() {
		System.out.print(this.off);
	}
}
