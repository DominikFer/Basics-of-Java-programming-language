package hr.fer.zemris.java.graphics.shapes;

public class Ellipse extends EllipseLikeShape {

	/**
	 * Create new ellipse with specified center, horizontal and vertical radius.
	 * 
	 * @param centerX X-coordinate of the ellipse center.
	 * @param centerY Y-coordinate of the ellipse center.
	 * @param horizontalRadius Horizontal radius of the ellipse.
	 * @param verticalRadius Vertical radius of the ellipse.
	 */
	public Ellipse(int centerX, int centerY, int horizontalRadius, int verticalRadius) {
		if(horizontalRadius < 1 || verticalRadius < 1) {
			throw new IllegalArgumentException();
		}
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.horizontalRadius = horizontalRadius;
		this.verticalRadius = verticalRadius;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		return Math.pow(x-this.centerX, 2)/Math.pow(this.horizontalRadius, 2) + Math.pow(y-this.centerY, 2)/Math.pow(this.verticalRadius, 2) <= 1;
	}

	/**
	 * @return Horizontal ellipse radius.
	 */
	public int getHorizontalRadius() {
		return horizontalRadius;
	}

	/**
	 * Set the horizontal radius of the ellipse.
	 * 
	 * @param horizontalRadius Horizontal ellipse radius.
	 */
	public void setHorizontalRadius(int horizontalRadius) {
		if(horizontalRadius < 1) {
			throw new IllegalArgumentException();
		}
		
		this.horizontalRadius = horizontalRadius;
	}

	/**
	 * @return Vertical ellipse radius.
	 */
	public int getVerticalRadius() {
		return verticalRadius;
	}

	/**
	 * Set the vertical radius of the ellipse.
	 * 
	 * @param verticalRadius Vertical ellipse radius.
	 */
	public void setVerticalRadius(int verticalRadius) {
		if(verticalRadius < 1) {
			throw new IllegalArgumentException();
		}
		
		this.verticalRadius = verticalRadius;
	}

}
