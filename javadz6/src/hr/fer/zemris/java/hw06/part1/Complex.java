package hr.fer.zemris.java.hw06.part1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which defines Complex number (x+yi).
 */
public class Complex {
	
	/** real part **/
	private double re;
	/** imaginary part **/
	private double im;
	
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0,-1);

	/**
	 * Default complex number constructor, sets real and imaginary part to 0.
	 */
	public Complex() {
		this.re = 0;
		this.im = 0;
	}
	
	/**
	 * Constructor for complex number.
	 * 
	 * @param re	Real part of complex number.
	 * @param im	Imaginary part of complex number.
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * Calculates the module of complex number.
	 * 
	 * @return Module of the complex number.
	 */
	public double module() {
		return Math.sqrt(this.re*this.re + this.im*this.im);
	}
	
	/**
	 * Multiplies this*c.
	 * 
	 * @param c	{@link Complex} number to be a multiplier.
	 * @return	Multiplication result.
	 */
	public Complex multiply(Complex c) {
		double re = this.re * c.re - this.im * c.im;
		double im = this.re * c.im + this.im * c.re;
		
		return new Complex(re, im);
	}
	
	/**
	 * Divides this/c.
	 * 
	 * @param c	{@link Complex} number which will be denominator.
	 * @return	Division result.
	 */
	public Complex divide(Complex c) {
		double moduleSquare = c.re*c.re + c.im*c.im;
		Complex cConjugated = new Complex(c.re, -c.im);
		
		return this.multiply(cConjugated).multiply(new Complex(1/moduleSquare, 0));
	}
	
	/**
	 * Adds this+c;
	 * 
	 * @param c	{@link Complex} number you want to add.
	 * @return	Addition result.
	 */
	public Complex add(Complex c) {
		double re = this.re + c.re;
		double im = this.im + c.im;
		
		return new Complex(re, im);
	}

	/**
	 * Subtracts this-c.
	 * 
	 * @param c	{@link Complex} number you want to subtract.
	 * @return	Subtraction result.
	 */
	public Complex sub(Complex c) {
		double re = this.re - c.re;
		double im = this.im - c.im;
		
		return new Complex(re, im);
	}
	
	/**
	 * Negates {@link Complex} number.
	 * 
	 * @return	Negated complex number.
	 */
	public Complex negate() {
		return this.multiply(ONE_NEG);
	}
	
	/**
	 * Parses the user input (string) as a complex number (x+yi).
	 * 
	 * @param complex	String which represents complex number (for ex. 1, -i, 3+i).
	 * @return			{@link Complex} number if parsing went OK, <code>null</code> otherwise.
	 */
	public static Complex parseString(String complex) {
		Pattern complexNumberPattern = Pattern.compile(
				// real part
				"([+-]?\\d+)?" +
				// imaginary part		
				"([+-]?i\\d*)?"
		);
		complex = complex.replace(" ", ""); // remove blanks (spaces)
		Matcher matches = complexNumberPattern.matcher(complex);
		
		if (matches.find()) {
			boolean reFound = false;
			double re = 0;
			if ((matches.group(1) != null) && (matches.group(1).length() > 0)) {
				reFound = true;
				
				try {
					re = Double.parseDouble(matches.group(1));
				} catch (Exception parseException) {
					System.out.println("Cannot parse '" + re + "' as double.");
				}
			}
			
			boolean imFound = false;
			double im = 0;
			if ((matches.group(2) != null) && (matches.group(2).length() > 0)){
				imFound = true;
				
				String imString = matches.group(2).replace("i", "");
				
				// Check if only "i" or "-i" is provided as input - add value of 1
				if (imString.length() <= 1) {
					imString = imString + "1";
				}
				
				try {
					im = Double.parseDouble(imString);
				} catch (Exception parseException) {
					System.out.println("Cannot parse '" + im + "' as double.");
				}
			}
			
			if(!reFound && !imFound) // Input is something like "asdasdi"
				return null;
			else
				return new Complex(re, im);
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the real part of complex number.
	 */
	public double getRe() {
		return re;
	}

	/**
	 * @return Returns the imaginary part of complex number.
	 */
	public double getIm() {
		return im;
	}
	
	@Override
	public String toString() {
		return this.re + (this.im >= 0 ? "+" : "") + this.im + "i";
	}
}