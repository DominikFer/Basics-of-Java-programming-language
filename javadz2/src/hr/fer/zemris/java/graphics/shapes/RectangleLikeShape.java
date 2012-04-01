package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public abstract class RectangleLikeShape extends GeometricShape {

	protected int topLeftX;
	protected int topLeftY;
	protected int width;
	protected int height;
	
	@Override
	public void draw(BWRaster r) {
		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				r.turnOn(x, y);
			}
		}
	};

	@Override
	public boolean containsPoint(int x, int y) {
		if(x < this.topLeftX) return false;
		if(y < this.topLeftY) return false;
		if(x >= this.topLeftX+this.width) return false;
		if(y >= this.topLeftY+this.height) return false;
		
		return true;
	}

	/**
	 * @return X-coordinate of the top left corner.
	 */
	public int getTopLeftX() {
		return topLeftX;
	}

	/**
	 * Set the X-coordinate of the top left corner.
	 * 
	 * @param topLeftX X-coordinate of the top left corner.
	 */
	public void setTopLeftX(int topLeftX) {
		this.topLeftX = topLeftX;
	}

	/**
	 * @return Y-coordinate of the top left corner.
	 */
	public int getTopLeftY() {
		return topLeftY;
	}

	/**
	 * Set the Y-coordinate of the top left corner.
	 * 
	 * @param topLeftX Y-coordinate of the top left corner.
	 */
	public void setTopLeftY(int topLeftY) {
		this.topLeftY = topLeftY;
	}
}
