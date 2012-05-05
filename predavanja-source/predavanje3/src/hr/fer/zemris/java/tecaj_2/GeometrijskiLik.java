package hr.fer.zemris.java.tecaj_2;

public class GeometrijskiLik {
	
	private String ime;
	
	public GeometrijskiLik(String ime) {
		this.ime = ime;
	}

	public String getIme() {
		return this.ime;
	}
	
	public double getOpseg() {
		return 0.0;
	}
	
	public double getPovrsina() {
		return 0.0;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof GeometrijskiLik)) return false;
		GeometrijskiLik drugi = (GeometrijskiLik) obj;
		return ime.equals(drugi.ime);
	}
	
	public String toString() {
		return "Lik " + ime;
	}
}