package hr.fer.zemris.java.tecaj_6;

public class PokretacDretve2 {

	public static void main(String[] args) {
		
		Posao p = new Posao();
		
		new Thread(p).start();
		new Thread(p).start();
		
	}

}
