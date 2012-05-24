package hr.fer.zemris.java.tecaj_11.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPKlijent {

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
		DatagramSocket socket = new DatagramSocket();
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			String redak = sc.nextLine();
			redak = redak.trim();
			if(redak.equals("gotovo")) break;
			
			byte[] buffer = redak.getBytes(StandardCharsets.UTF_8);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			packet.setAddress(adresa);
			packet.setPort(portServera);
			
			while(true) {
				socket.send(packet);
				
				byte[] buffer2 = new byte[1024];
				DatagramPacket paketZaOdgovor = new DatagramPacket(buffer2, buffer2.length);
				
				socket.setSoTimeout(5000);
				try {
					socket.receive(paketZaOdgovor);
				} catch(SocketTimeoutException ex) {
					continue;
				}
				
				String rjesenje = new String(
						paketZaOdgovor.getData(),
						paketZaOdgovor.getOffset(), 
						paketZaOdgovor.getLength(),
						StandardCharsets.UTF_8
				);
				
				System.out.println("Odgovor je: " + rjesenje);
				
				break;
			}
		}
		
		socket.close();
	}
	
}
