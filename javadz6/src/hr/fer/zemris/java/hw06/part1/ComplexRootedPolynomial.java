package hr.fer.zemris.java.hw06.part1;

/**
 * Class which represents complex number by its roots.
 */
public class ComplexRootedPolynomial {

	private Complex[] roots;
	
	/**
	 * Creates new {@link ComplexRootedPolynomial} with provided roots.
	 * 
	 * @param roots	Defined roots for new ComplexRootedPolynomial.
	 */
	public ComplexRootedPolynomial(Complex ... roots) {
		this.roots = roots;
	}
	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z	Point for which you want to calculate polynomial value.
	 * @return	Returns polynomial value at given point z.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;
		
		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		
		return result;
	}
	
	/**
	 * Converts this representation to {@link ComplexPolynomial} type.
	 * 
	 * @return	{@link ComplexRootedPolynomial} in {@link ComplexPolynomial} type.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);
		
		for (Complex root : roots) {
			result = result.multiply(new ComplexPolynomial(Complex.ONE, root.negate()));
		}
		
		return result;
	}
	
	/**
	 * Finds index of closest root for given complex number z that is within threshold.
	 * If there is no such root, returns -1.
	 * 
	 * @param z				Complex number for which you want to calculate closest root.
	 * @param threshold		Threshold inside which roots will be calculated.
	 * @return				Index of the closest root if it's within threshold, -1 otherwise.
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int index = -1;
		double minDistance = Double.MAX_VALUE;
		
		for (int i = 0; i < roots.length; ++i) {
			double distance = z.sub(roots[i]).module();
			if (Double.compare(distance, threshold) < 0 && Double.compare(distance, minDistance) < 0) {
				minDistance = distance;
				index = i;
			}
		}
		
		return index;
	}
	
	/**
	 * @return Returns roots of this ComplexRootedPolynomial.
	 */
	public Complex[] getRoots() {
		return this.roots;
	}

	/**
	 * @return Returns order of this ComplexRootedPolynomial.
	 */
	public int order() {
		return this.roots.length;
	}
	
	@Override
	public String toString() {
		 String result = "(no roots)";
         
		 if(roots.length > 0) {
			 result = "";
			 for (Complex root : roots) {
	        	 result += "[z-(" + root + ")]";
	         }
		 }
         
         return result;
	}
}