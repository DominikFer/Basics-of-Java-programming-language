package hr.fer.zemris.java.tecaj_5;

import java.util.ArrayList;
import java.util.List;

public class DohvatZaposlenika {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Zaposlenik> zaposlenici = new ArrayList<>();
		
		Zaposlenik peric = new Zaposlenik("1", "Pero", "Peric", 10000);
		zaposlenici.add(0, peric);
		zaposlenici.add(0, new Zaposlenik("2", "Agata", "Agic", 15000));
		zaposlenici.add(0, new Zaposlenik("3", "Ivana", "Ivic", 20000));
		
		for(Zaposlenik zaposlenik : zaposlenici) {
			System.out.println(zaposlenik);
		}
		
		System.out.println();
		System.out.println("Zaposlenik na mjestu 2 je " + zaposlenici.get(2));
		
		Zaposlenik zaposlenik = new Zaposlenik("1", "Pero", "Peric", 10000);
		boolean nekiJeUListi = zaposlenici.contains(zaposlenik);
		boolean pericJeUListi = zaposlenici.contains(peric);
		
		System.out.println();
		System.out.println("Lista zaposlenika sadrži traženog: " + nekiJeUListi);
		System.out.println("Lista zaposlenika sadrži perica: " + pericJeUListi);
	}

}
