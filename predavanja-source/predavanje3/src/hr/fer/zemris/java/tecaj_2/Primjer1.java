package hr.fer.zemris.java.tecaj_2;

public class Primjer1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeometrijskiLik lik = new GeometrijskiLik("Lik 1");
		GeometrijskiLik lik2 = new GeometrijskiLik("Lik 2");

		System.out.println("Ime lika 1 je " + lik.getIme());
		System.out.println("Ime lika 2 je " + lik2.getIme());
	}
}