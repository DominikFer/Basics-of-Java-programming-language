package hr.fer.zemris.java.graphics.shapes;


public class Circle extends EllipseLikeShape {

	/**
	 * Create new circle with specified center and radius.
	 * 
	 * @param centerX X-coordinate of the circle center.
	 * @param centerY Y-coordinate of the circle center.
	 * @param radius Circle radius.
	 */
	public Circle(int centerX, int centerY, int radius) {
		if(radius < 1) {
			throw new IllegalArgumentException();
		}
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.horizontalRadius = radius;
		this.verticalRadius = radius;
	}
	
	@Override
	public boolean containsPoint(int x, int y) {
		return Math.pow(x-this.centerX, 2) + Math.pow(y - this.centerY, 2) <= Math.pow(this.horizontalRadius, 2);
	}

	/**
	 * @return Circle radius.
	 */
	public int getRadius() {
		return this.horizontalRadius;
	}

	/**
	 * Set the circle radius.
	 * 
	 * @param radius Circle radius.
	 */
	public void setRadius(int radius) {
		if(radius < 1) {
			throw new IllegalArgumentException();
		}
		
		this.horizontalRadius = radius;
		this.verticalRadius = radius;
	}

}
