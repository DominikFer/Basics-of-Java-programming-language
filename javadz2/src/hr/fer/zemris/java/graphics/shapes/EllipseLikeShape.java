package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public abstract class EllipseLikeShape extends GeometricShape {

	protected int centerX;
	protected int centerY;
	protected int horizontalRadius;
	protected int verticalRadius;
	
	@Override
	public void draw(BWRaster r) {
		int topLeftX = this.centerX - this.horizontalRadius;
		int topLeftY = this.centerY - this.verticalRadius;
		
		int width = topLeftX + this.horizontalRadius*2;
		int height = topLeftY + this.verticalRadius*2;
		
		for(int y = topLeftY; y < height; y++) {
			for(int x = topLeftX; x < width; x++) {
				if(this.containsPoint(x, y))
					r.turnOn(x, y);
			}
		}
	};
	
	/**
	 * @return Center X-coordinate of the shape.
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * Set the center X-coordinate of the shape.
	 * 
	 * @param centerX Center X-coordinate of the shape.
	 */
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	/**
	 * @return Center Y-coordinate of the shape.
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * Set the center Y-coordinate of the shape.
	 * 
	 * @param centerX Center Y-coordinate of the shape.
	 */
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
}
