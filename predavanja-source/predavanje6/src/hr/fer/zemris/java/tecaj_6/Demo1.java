package hr.fer.zemris.java.tecaj_6;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo1 {

	
	
	public static void main(String[] args) {
		double s1 = suma(1.0, 2.0, 5.0, -4.0);
		double s2 = suma(1.0, -15.0, -4.0);
		double s3 = suma(2.0, -5.0);
		
		final int granica = args.length;
		
		// Operacija.ZBRAJANJE: int = 0
		// Operacija.ODUZIMANJE: int = 1
		// Operacija.MNOZENJE: int = 2
		
		Operacije op = Operacije.PODIJELI;
		
		Path p = Paths.get("D:/tmp/javaPrimjeri");
	}

	public static double suma(double ... brojevi) {
		System.out.println("Broj argumenata je: " + brojevi.length);
		double s = 0.0;
		for(double d : brojevi) {
			s += d;
		}
		return s;
	}
	
	
}
