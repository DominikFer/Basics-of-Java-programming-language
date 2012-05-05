package hr.fer.zemris.java.tecaj_1;

/**
 * Ovaj program služi za demonstraciju dohvata argumenata iz komadne linije.
 * 
 * @author sven
 *
 */
public class IspisArgumenata {
	
	/**
	 * Ovo je metoda od koje kreće izvodenje programa.
	 * 
	 * @param args ovo su argumenti predani iz komadne linije
	 */
	public static void main(String[] args) {
		int brojArgumenata = args.length;
		for(int i = 0; i < brojArgumenata; i++) {
			System.out.println("Argument " + (i+1) + ": "  + args[i]);
		}
	}
}