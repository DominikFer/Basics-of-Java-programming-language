package opjj.hw1;

import static org.junit.Assert.*;

import org.junit.Test;

public class FactorialTest {

	@Test
	public void factorialOfN() {
		assertEquals("n(0) == 1", 1, Factorial.calc(0));
		assertEquals("n(1) == 1", 1, Factorial.calc(1));
		assertEquals("n(2) == 2", 2, Factorial.calc(2));
		assertEquals("n(3) == 3", 6, Factorial.calc(3));
	}
	
}
