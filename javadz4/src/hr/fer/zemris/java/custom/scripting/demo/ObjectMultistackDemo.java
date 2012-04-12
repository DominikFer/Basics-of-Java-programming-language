package hr.fer.zemris.java.custom.scripting.demo;

public class ObjectMultistackDemo {

	public static void main(String[] args) {
//		ObjectMultistack multistack = new ObjectMultistack();
//		ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
//		multistack.push("year", year);
//		ValueWrapper price = new ValueWrapper(200.51);
//		multistack.push("price", price);
//		System.out.println("1Current value for year: " + multistack.peek("year").getValue());
//		System.out.println("2Current value for price: " + multistack.peek("price").getValue());
//		multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
//		System.out.println("3Current value for year: " + multistack.peek("year").getValue());
//		multistack.peek("year").setValue(
//				((Integer) multistack.peek("year").getValue()).intValue() + 50);
//		System.out.println("4Current value for year: " + multistack.peek("year").getValue());
//		multistack.pop("year");
//		System.out.println("5Current value for year: " + multistack.peek("year").getValue());
//		multistack.peek("year").increment("5");
//		System.out.println("6Current value for year: " + multistack.peek("year").getValue());
//		multistack.peek("year").increment(5);
//		System.out.println("7Current value for year: " + multistack.peek("year").getValue());
//		multistack.peek("year").increment(5.0);
//		System.out.println("8Current value for year: " + multistack.peek("year").getValue());
		
		ValueWrapper first = new ValueWrapper(null);
		System.out.println(first.numCompare(5));
	}
}