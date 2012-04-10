package hr.fer.zemris.java.tecaj_3;

public class ProbaPointera {

	public static void main(String[] args) {
		Integer broj1 = new Integer(10);
		
		Pointer<Integer> pBroj1 = new Pointer<Integer>(broj1);
		System.out.println("Broj1 (prije) = " + broj1);
		uvecaj2(pBroj1);
		broj1 = pBroj1.getObject();
		System.out.println("Broj1 (nakon) = " + broj1);
	}

	private static void uvecaj2(Pointer<Integer> pBroj1) {
		pBroj1.setObject(Integer.valueOf(pBroj1.getObject().intValue()+1));
	}
}