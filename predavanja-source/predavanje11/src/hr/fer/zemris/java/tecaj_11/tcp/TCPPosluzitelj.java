package hr.fer.zemris.java.tecaj_11.tcp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPPosluzitelj {

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
		
		ServerSocket ssocket = new ServerSocket(serverPort);
		
		while(true) {
			
			Socket csocket = ssocket.accept();
			new Thread(new ObradaKlijenta(csocket)).start();
		}
	}
	
	private static class ObradaKlijenta implements Runnable {
		
		private Socket csocket;
		
		public ObradaKlijenta(Socket csocket) {
			this.csocket = csocket;
		}
		
		@Override
		public void run() {
			try {
				InputStream is = new BufferedInputStream(csocket.getInputStream());
				Scanner sc = new Scanner(is, "UTF-8");
				while(true) {
					String naredba = sc.nextLine();
					if(naredba.equals("quit")) break;
					
					String odgovor = izracunajOdgovor(naredba);
					csocket.getOutputStream().write(odgovor.getBytes(StandardCharsets.UTF_8));
					csocket.getOutputStream().write((byte) '\n');
					csocket.getOutputStream().flush();
				}
			} catch (IOException e) {}
			
			try {csocket.close();} catch(Exception ignorable) {}
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
}
