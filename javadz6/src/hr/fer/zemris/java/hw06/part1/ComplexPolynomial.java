package hr.fer.zemris.java.hw06.part1;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents complex number in polynomial form.
 */
public class ComplexPolynomial {
	
	private Complex[] factors;
	
	/**
	 * Creates new {@link ComplexPolynomial} with defined factors.
	 * 
	 * @param factors	Complex factors.
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.factors = factors;
	}
	
	/**
	 * @return Returns the order of the ComplexPolynomial.
	 */
	public short order() {
		return (short) (factors.length-1);
	}
	
	/**
	 * Multiplies this * p.
	 * 
	 * @param p	ComplexPolynomial which will be multiplier.
	 * @return	Multiplication result.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] multipliedFactors = new Complex[this.factors.length + p.getFactors().length - 1];
		
		// Initialize to zero
		for (int i = 0; i < multipliedFactors.length; i++) {
			multipliedFactors[i] = Complex.ZERO;
		}
		
		// Multiply
		for (int i = 0; i < this.factors.length; i++) {
			for (int j = 0; j < p.getFactors().length; j++) {
            	Complex multiplicationResult = this.factors[i].multiply(p.factors[j]);
            	multipliedFactors[i+j] = multipliedFactors[i+j].add(multiplicationResult);
            }
		}
		
		return new ComplexPolynomial(multipliedFactors);
	}
	
	/**
	 * Computes first derivative of ComplexPolynomial.
	 * 
	 * @return	Returns first derivation of ComplexPolynomial.
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedFactors = new Complex[this.getFactors().length - 1];
		
		for (int i = 0; i < this.order(); i++) {
			derivedFactors[i] = this.factors[i].multiply(new Complex(this.order() - i, 0));
		}

		return new ComplexPolynomial(derivedFactors);
	}
	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z	Point for which you want to calculate polynomial value.
	 * @return	Returns polynomial value at given point z.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		Complex exponent = Complex.ONE;
		
		for (int i = this.factors.length - 1; i >= 0; i--) {
			result = result.add(exponent.multiply(this.factors[i]));
			exponent = exponent.multiply(z);
		}
		
		return result;
	}
	
	/**
	 * @return Factors of this ComplexPolynomial.
	 */
	public Complex[] getFactors() {
		return this.factors;
	}
	
	@Override
	public String toString() {
		int order = this.order();
		List<String> polynomFactors = new ArrayList<String>();
		
		for (Complex factor : factors) {
			if (factor.equals(Complex.ZERO))
				continue;
            
			String factorString = "(" + factor + ")";
			
			if (order == 1) {
            	factorString += "z";
            } else if (order > 1) {
            	factorString += "z^" + order;
            }
			
            polynomFactors.add(factorString);
            
            order--;
        }
		
		String result = "(no factors)";
		
		if(polynomFactors.size() > 0) {
			result = polynomFactors.get(0);
			for (int i = 1; i < polynomFactors.size(); i++) {
				result += " + ";
				result += polynomFactors.get(i);
			}
		}
		
        return result;
    }
}