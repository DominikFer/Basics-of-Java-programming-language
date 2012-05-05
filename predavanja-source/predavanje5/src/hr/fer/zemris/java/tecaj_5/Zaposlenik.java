package hr.fer.zemris.java.tecaj_5;

public class Zaposlenik implements Comparable<Zaposlenik> {
	
	private String sifra;
	private String ime;
	private String prezime;
	private double placa;
	
	public Zaposlenik() {
	}

	public Zaposlenik(String sifra, String ime, String prezime, double placa) {
		super();
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
		this.placa = placa;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public double getPlaca() {
		return placa;
	}

	public void setPlaca(double placa) {
		this.placa = placa;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof Zaposlenik)) return false;
		Zaposlenik drugi = (Zaposlenik) obj;
		return this.sifra.trim().equals(drugi.sifra.trim());
	}
	
	@Override
	public String toString() {
		return "Zaposlenik: " + this.sifra +", ime = " + this.ime +
				", prezime = " + this.prezime + ", placa = " + this.placa;
	}

	@Override
	public int compareTo(Zaposlenik o) {
		return this.sifra.trim().compareTo(o.sifra.trim());
	}
	
}