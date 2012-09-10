package opjj.hw4;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReversePolishCalculatorTest {

	private ReversePolishCalculator calculator = new ReversePolishCalculator();
	
	@Test
	public void addition() {
		assertEquals("12+", 3, calculator.calc("12+"));
		calculator.clear();
		assertEquals("123++", 6, calculator.calc("123++"));
	}
	
	@Test
	public void multiplication() {
		assertEquals("57*", 35, calculator.calc("57*"));
		calculator.clear();
		assertEquals("50*", 0, calculator.calc("50*"));
	}
	
	@Test
	public void subtraction() {
		assertEquals("79-", 2, calculator.calc("79-"));
	}
	
	@Test
	public void division() {
		assertEquals("34/", 1, calculator.calc("34/"));
	}
	
	@Test
	public void calculateWithSingleValue() {
		assertEquals("1", 1, calculator.calc("1"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalAddition() {
		assertEquals("123+", 1, calculator.calc("123+"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void divisionWithZero() {
		assertEquals("05/", 1, calculator.calc("05/"));
	}
	
}
