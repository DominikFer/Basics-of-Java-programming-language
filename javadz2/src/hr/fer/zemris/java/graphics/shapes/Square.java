package hr.fer.zemris.java.graphics.shapes;


public class Square extends RectangleLikeShape {

	/**
	 * Create new square with specific coordinate of the top-left corner and size (width and height).
	 * 
	 * @param topLeftX X-coordinate of the top-left corner.
	 * @param topLeftY Y-coordinate of the top-left corner.
	 * @param size Square size (width and height).
	 */
	public Square(int topLeftX, int topLeftY, int size) {
		if(size <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.width = size;
		this.height = size;
	}

	/**
	 * @return Square size (width and height).
	 */
	public int getSize() {
		return this.width;
	}

	/**
	 * Set the square size.
	 * 
	 * @param size Square size dimensions (width and height).
	 */
	public void setSize(int size) {
		if(size <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.width = size;
		this.height = size;
	}

}
