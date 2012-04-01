package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class Triangle extends GeometricShape {

	private int ax;
	private int ay;
	private int bx;
	private int by;
	private int cx;
	private int cy;
	
	/**
	 * Creates new triangle with 3 specific corners/vertices (A, B and C).
	 * 
	 * @param ax X-coordinate of the A corner.
	 * @param ay Y-coordinate of the A corner.
	 * @param bx X-coordinate of the B corner.
	 * @param by Y-coordinate of the B corner.
	 * @param cx X-coordinate of the C corner.
	 * @param cy Y-coordinate of the C corner.
	 */
	public Triangle(int ax, int ay, int bx, int by, int cx, int cy) {
		this.ax = ax;
		this.ay = ay;
		this.bx = bx;
		this.by = by;
		this.cx = cx;
		this.cy = cy;
	}
	
	@Override
	public void draw(BWRaster r) {
		int topLeftX = Math.min(ax, Math.min(bx, cx));
		int topLeftY = Math.min(ay, Math.min(by, cy));
		
		int width = Math.max(ax, Math.max(bx, cx));
		int height = Math.max(ay, Math.max(by, cy));
		
		for(int y = topLeftY; y < height; y++) {
			for(int x = topLeftX; x < width; x++) {
				if(this.containsPoint(x, y))
					r.turnOn(x, y);
			}
		}
	};
	
	@Override
	public boolean containsPoint(int x, int y) {
		float ccw = this.ccw(this.ax, this.ay, this.bx, this.by, this.cx, this.cy);
		if(ccw > 0) {
			return this.ccw(this.ax, this.ay, this.bx, this.by, x, y) >= 0 && this.ccw(this.bx, this.by, this.cx, this.cy, x, y) >= 0 && this.ccw(this.cx, this.cy, this.ax, this.ay, x, y) >= 0;
		} else if (ccw < 0) {
			return this.ccw(this.ax, this.ay, this.bx, this.by, x, y) <= 0 && this.ccw(this.bx, this.by, this.cx, this.cy, x, y) <= 0 && this.ccw(this.cx, this.cy, this.ax, this.ay, x, y) <= 0;
		} else {
			return false;
		}
	}
	
	/**
	 * Calculation used to find out if corners are going in clock-wise or counter clock-wise direction.
	 * 
	 * @param ax X-coordinate of the A corner.
	 * @param ay Y-coordinate of the A corner.
	 * @param bx X-coordinate of the B corner.
	 * @param by Y-coordinate of the B corner.
	 * @param cx X-coordinate of the C corner.
	 * @param cy Y-coordinate of the C corner.
	 * @return Number smaller than <code>0.0</code> means they are going in clock-wise direction,
	 * number larger than <code>0.0</code> means they are going in counter clock-wise direction,
	 * <code>0.0</code> means they are on the straight line.
	 */
	private float ccw(int ax, int ay, int bx, int by, int cx, int cy) {
		return Math.signum((bx-ax)*(cy-ay)-(by-ay)*(cx-ax));
	}

	/**
	 * @return X-coordinate of the A corner.
	 */
	public int getAx() {
		return ax;
	}

	/**
	 * Set the X-coordinate of the A corner.
	 * 
	 * @param ax X-coordinate of the A corner.
	 */
	public void setAx(int ax) {
		this.ax = ax;
	}

	/**
	 * @return Y-coordinate of the A corner.
	 */
	public int getAy() {
		return ay;
	}

	/**
	 * Set the Y-coordinate of the A corner.
	 * 
	 * @param ay Y-coordinate of the A corner.
	 */
	public void setAy(int ay) {
		this.ay = ay;
	}

	/**
	 * @return X-coordinate of the B corner.
	 */
	public int getBx() {
		return bx;
	}

	/**
	 * Set the X-coordinate of the B corner.
	 * 
	 * @param bx X-coordinate of the B corner.
	 */
	public void setBx(int bx) {
		this.bx = bx;
	}

	/**
	 * @return Y-coordinate of the B corner.
	 */
	public int getBy() {
		return by;
	}

	/**
	 * Set the Y-coordinate of the B corner.
	 * 
	 * @param by Y-coordinate of the B corner.
	 */
	public void setBy(int by) {
		this.by = by;
	}

	/**
	 * @return X-coordinate of the C corner.
	 */
	public int getCx() {
		return cx;
	}

	/**
	 * Set the X-coordinate of the C corner.
	 * 
	 * @param cx X-coordinate of the C corner.
	 */
	public void setCx(int cx) {
		this.cx = cx;
	}

	/**
	 * @return Y-coordinate of the C corner.
	 */
	public int getCy() {
		return cy;
	}

	/**
	 * Set the Y-coordinate of the C corner.
	 * 
	 * @param cy Y-coordinate of the C corner.
	 */
	public void setCy(int cy) {
		this.cy = cy;
	}

}
