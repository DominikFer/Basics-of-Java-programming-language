package hr.fer.zemris.java.tecaj_3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Pravokutnik extends GeometrijskiLik {
	
	/**
	 * sdasda
	 */
	private int topLeftX;
	private int topLeftY;
	private int width;
	private int height;
	
	public Pravokutnik(int topLeftX, int topLeftY, int width, int height) {
		super();
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean sadrziTocku(int x, int y) {
		if(x < topLeftX) return false;
		if(y < topLeftY) return false;
		if(x >= topLeftX+width) return false;
		if(y >= topLeftY+height) return false;
		return true;
	}
	
	@Override
	public void popuniLik(Slika slika) {
		int minX = topLeftX;
		int maxX = topLeftX+width-1;
		int minY = topLeftY;
		int maxY = topLeftX+height-1;
		
		for(int y = minY; y <= maxY; y++) {
			for(int x = minX; x <= maxX; x++) {
				slika.upaliTocku(x, y);
			}
		}
	}
}
