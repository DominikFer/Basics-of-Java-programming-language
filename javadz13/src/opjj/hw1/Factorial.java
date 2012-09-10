package opjj.hw1;

/**
 * Program which calculates factorial of n (or simply n!).
 */
public class Factorial {

    public static int calc(int n) {
        return factorial(n);
    }
    
    /**
     * Calculates factorial from <code>0</code> to <code>n</code>.
     * Because of <code>int</code> as a returning value the upper most
     * limit for <code>n</code> is 12.
     * 
     * @param n		n!
     * @return		Returns factorial of n
     */
    public static int factorial(int n) {
    	if(n > 12) throw new IllegalArgumentException("You cannot calculate factorial of '" + n + "' in Integer returning value.");
    	
    	if(n == 0) return 1;
    	return n * factorial(n-1);
    }

}
