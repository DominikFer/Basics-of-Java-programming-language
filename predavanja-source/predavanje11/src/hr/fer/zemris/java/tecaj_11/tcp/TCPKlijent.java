package hr.fer.zemris.java.tecaj_11.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPKlijent {

	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Očekivao sam 2 argumenta: ip adresu te port.");
			System.exit(-1);
		}
		
		String adresaServera = args[0];
		int portServera = 0;
		
		try {
			portServera = Integer.parseInt(args[1]);
		} catch(Exception ex) {
			System.err.println("Port nisam uspio pretvoriti u broj.");
			System.exit(-1);
		}
		
		InetAddress adresa = InetAddress.getByName(adresaServera);
		
		Socket socket = new Socket(adresa, portServera);
		
		Scanner sc = new Scanner(System.in);
		Scanner scPosluzitelja = new Scanner(socket.getInputStream(), "UTF-8");
		while(true) {
			String redak = sc.nextLine();
			redak = redak.trim();
			redak = redak + "\n";
			
			byte[] buffer = redak.getBytes(StandardCharsets.UTF_8);
			
			socket.getOutputStream().write(buffer);
			socket.getOutputStream().flush();
			
			String odgovor = scPosluzitelja.nextLine();
			System.out.println("Dobio sam odgovor: " + odgovor);
		}
		
	}
}
