package hr.fer.zemris.java.tecaj_1;

import java.util.Scanner;

public class Vjezba {

	/**
	 * Program koji vraća prim i fibonacijeve brojeve u zadnom rangu,
	 * od prvog do posljednjeg zadanog rednog broja.
	 * Parametri se učitavaju putem tipkovnice.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int prviRedniBroj = 0;
		int posljednjiRedniBroj = 0;
		
		Scanner s = new Scanner(System.in);
		
		do {
			System.out.println("Unesite prvi redni broj: ");
			prviRedniBroj = s.nextInt();
			System.out.println("Unesite posljednji redni broj: ");
			posljednjiRedniBroj = s.nextInt();
		} while(prviRedniBroj > posljednjiRedniBroj);
		s.close();
		
		for(int i = prviRedniBroj; i <= posljednjiRedniBroj; i++) {
			System.out.println(i + " " + dohvatiPrimBroj(i) + " " + dohvatiFibBroj(i));
		}
	}
	
	/**
	 * Metoda dohvaća prim broj na zadanom indexu.
	 * 
	 * @param index Redni broj na kojem želim dohvatiti prim broj.
	 * @return prim broj
	 */
	public static long dohvatiPrimBroj(int index) {
		int nPrimBrojeva = 0;
		boolean primBroj;
		
		if(index == 0)
			return 0;
		
		for(int i = 2; i > 0; i++) {
			primBroj = true;
			for(int j = 2; j < i; j++) {
				if(i % j == 0) {
					primBroj = false;
					break;
				}
			}
			if(primBroj) {
				nPrimBrojeva++;
				if(nPrimBrojeva == index)
					return i;
			}
		}
		
		return 0;
	}
	
	/**
	 * Metoda dohvaća fibbonacijev broj na zadanom indexu.
	 * 
	 * @param index Redni broj Fibbonacijevog broja kojeg želimo dohvatiti.
	 * @return Fibbonacijev broj na zadanom indexu
	 */
	public static long dohvatiFibBroj(int index) {
		long tmp;
		long first = 0, second = 1;
		
		while(index-- > 0) {
			tmp = first + second;
			first = second;
			second = tmp;
		}
		
		return first;
	}
}
