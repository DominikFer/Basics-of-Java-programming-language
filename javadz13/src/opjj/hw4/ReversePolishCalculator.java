package opjj.hw4;

import opjj.hw3.StackCalculator;

/**
 *	Calculator which uses RPC (Reverse Polish Notation) as it's syntax.
 *	Supported operations are +,-,* and /. 
 */
public class ReversePolishCalculator {

	private StackCalculator stack;
	
	/**
	 * Constructor.
	 */
	public ReversePolishCalculator() {
		stack = new StackCalculator();
	}
	
	/**
	 * Calculate the expression in RPC notation.
	 * 
	 * @param calculation	Expression in RPC notation
	 * @return				Returns final result.
	 */
    public int calc(String calculation) {
    	for(int i = 0; i < calculation.length(); i++) {
			try {
				int number = Integer.parseInt(Character.toString(calculation.charAt(i)));
				stack.push(number);
			} catch(NumberFormatException e) {
				char operation = calculation.charAt(i);
				if(operation == '+') {
					stack.add();
				} else if(operation == '-') {
					stack.minus();
				} else if(operation == '*') {
					stack.multiply();
				} else if(operation == '/') {
					stack.divide();
				} else {
					throw new IllegalArgumentException("Unsupported '" + operation + "' operation.");
				}
			}
		}
		
		if(stack.size() > 1) {
			throw new IllegalArgumentException("You don't have proper syntax in RPC notation (need more operations).");
		}
		
		return stack.result();
    }
    
    /**
     * Clears the calculator (reset).
     */
    public void clear() {
    	while(stack.size() > 0)
    		stack.pop();
    }

}
