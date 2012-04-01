package hr.fer.zemris.java.graphics.shapes;


public class Rectangle extends RectangleLikeShape {

	/**
	 * Create new rectangle with specific coordinate of the top-left corner, width and height.
	 * 
	 * @param topLeftX X-coordinate of the top-left corner.
	 * @param topLeftY Y-coordinate of the top-left corner.
	 * @param width Rectangle width.
	 * @param height Rectangle height.
	 */
	public Rectangle(int topLeftX, int topLeftY, int width, int height) {
		if(width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return Width of the rectangle.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the rectangle.
	 * 
	 * @param width Width of the rectangle.
	 */
	public void setWidth(int width) {
		if(width <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.width = width;
	}

	/**
	 * @return Height of the rectangle.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of the rectangle.
	 * 
	 * @param height Height of the rectangle.
	 */
	public void setHeight(int height) {
		if(height <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.height = height;
	}

}
