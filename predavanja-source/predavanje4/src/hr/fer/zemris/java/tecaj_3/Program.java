package hr.fer.zemris.java.tecaj_3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Program {
	
	public static void main(String[] args) {
		Slika slika = new Slika(300, 300);
		
		Pravokutnik p1 = new Pravokutnik(10, 10, 280, 50);
		Pravokutnik p2 = new Pravokutnik(10, 100, 280, 150);
		
		p1.popuniLik(slika);
		p2.popuniLik(slika);
		
		PrikaznikSlike.prikaziSliku(slika);
		
		List<String> list = new ArrayList<String>();
		for(String s : list) {
			
		}
		list.clear();

		
	}
}