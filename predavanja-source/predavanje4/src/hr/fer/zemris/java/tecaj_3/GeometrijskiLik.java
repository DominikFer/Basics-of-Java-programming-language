package hr.fer.zemris.java.tecaj_3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public abstract class GeometrijskiLik {
	
	public abstract boolean sadrziTocku(int x, int y);
	
	public void popuniLik(Slika slika) {
		int maxY = slika.getVisina();
		int maxX = slika.getSirina();
		
		for(int y = 0; y < maxY; y++) {
			for(int x = 0; x < maxX; x++) {
				if(this.sadrziTocku(x, y)) {
					slika.upaliTocku(x, y);
				}
			}
		}
	}
}