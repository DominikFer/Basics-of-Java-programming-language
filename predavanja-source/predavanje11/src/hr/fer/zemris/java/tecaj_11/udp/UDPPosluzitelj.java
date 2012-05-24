package hr.fer.zemris.java.tecaj_11.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPPosluzitelj {

	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.err.println("Očekivao sam jedan argument: port.");
			System.exit(-1);
		}
		
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			System.err.println("Niste zadali valjani port.");
			System.exit(-1);
		}
		
		DatagramSocket socket = new DatagramSocket(serverPort);
		
		System.out.println("Poslužitelj sluša na adresi: " + socket.getLocalSocketAddress());
		while(true) {
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			
			System.out.println("Dobio sam upit od:" + packet.getSocketAddress());
			String upit = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);;
			
			String odgovor = izracunajOdgovor(upit);
			
			byte[] oktetiOdgovora = odgovor.getBytes(StandardCharsets.UTF_8);
			DatagramPacket paketOdgovora = new DatagramPacket(oktetiOdgovora, oktetiOdgovora.length);
			paketOdgovora.setAddress(packet.getAddress());
			paketOdgovora.setPort(packet.getPort());
		}
	}
	
	private static String izracunajOdgovor(String upit) {
		try {
			String[] elems = upit.split(" ");
			double a = Double.parseDouble(elems[0]);
			double b = Double.parseDouble(elems[2]);
			if(elems[1].equals("+")) {
				return Double.toString(a+b);
			} else if(elems[1].equals("-")) {
				return Double.toString(a-b);
			}
		} catch (Exception ex) {}
		
		return "ERR";
	}
}
