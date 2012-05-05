package hr.fer.zemris.java.tecaj_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ProgramStabla {

	public static void main(String[] args) {
		
		Comparator<Zaposlenik> komparator = new KompozitniKomparator()
			.dodaj(new KomparatorZaposlenikaPoPlaci())
			.dodaj(Collections.reverseOrder(new KomparatorZaposlenikaPoPrezimenu()))
			.dodaj(new KomparatorZaposlenikaPoSiframa());
		
		Set<Zaposlenik> zaposlenici = new TreeSet<Zaposlenik>(new Comparator<Zaposlenik>() {
			@Override
			public int compare(Zaposlenik o1, Zaposlenik o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		Zaposlenik peric = new Zaposlenik("1", "Pero", "Peric", 10000);
		
		zaposlenici.add(new Zaposlenik("2", "Agata", "Agic", 15000));
		zaposlenici.add(new Zaposlenik("4", "Vedrana", "Vedrić", 15000));
		zaposlenici.add(peric);
		zaposlenici.add(new Zaposlenik("3", "Ivana", "Ivic", 12500));
		
		for(Zaposlenik zaposlenik : zaposlenici) {
			System.out.println(zaposlenik);
		}
	}
	
	
	
	private static class ReverzniKomparator implements Comparator<Zaposlenik> {
		private Comparator<Zaposlenik> izvorniKomparator;

		public ReverzniKomparator(Comparator<Zaposlenik> izvorniKomparator) {
			super();
			this.izvorniKomparator = izvorniKomparator;
		}

		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			return -izvorniKomparator.compare(o1, o2);
		}
	}
	
	private static class KomparatorZaposlenikaPoPrezimenu implements Comparator<Zaposlenik> {
		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			return o1.getPrezime().compareTo(o2.getPrezime());
		}
	}
	
	private static class KomparatorZaposlenikaPoImenu implements Comparator<Zaposlenik> {
		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			return o1.getIme().compareTo(o2.getIme());
		}
	}
	
	private static class KomparatorZaposlenikaPoSiframa implements Comparator<Zaposlenik> {
		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			return o1.compareTo(o2);
		}
	}
	
	private static class KomparatorZaposlenikaPoPlaci implements Comparator<Zaposlenik> {
		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			if(Math.abs(o1.getPlaca()-o2.getPlaca()) < 0.01) {
				return 0;
			}
			if(o1.getPlaca()-o2.getPlaca() < 0)
				return -1;
			
			return 1;
		}
	}
	
	private static class KompozitniKomparator implements Comparator<Zaposlenik> {
		private List<Comparator<Zaposlenik>> komparatori;
		
		public KompozitniKomparator() {
			komparatori = new ArrayList<Comparator<Zaposlenik>>();
		}
		
		public KompozitniKomparator dodaj(Comparator<Zaposlenik> komparator) {
			komparatori.add(komparator);
			return this;
		}

		@Override
		public int compare(Zaposlenik o1, Zaposlenik o2) {
			for(Comparator<Zaposlenik> komparator : komparatori) {
				int rezultat = komparator.compare(o1, o2);
				if(rezultat != 0) return rezultat;
			}
			
			return 0;
		}
	}
}
