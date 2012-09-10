package opjj.hw3;

import java.util.Stack;

/**
 * Calculator that uses stack as it's base. Supported basic 4 operations:
 * addition, subtraction, multiplication and division.
 */
public final class StackCalculator {

    private final Stack<Integer> stack = new Stack<Integer>();

    /**
     * Push the value on the top of the stack.
     * 
     * @param n	Value you want to push.
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator push(int n) {
        stack.push(n);
        return this;
    }
    
    /**
     * Pop the value of the stack.
     * 
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator pop() {
        stack.pop();
        return this;
    }
    
    /**
     * Add two numbers and put the result on the stack.
     * 
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator add() {
    	operation(Operation.ADDITION);
        return this;
    }

    /**
     * Subtract two numbers and put the result on the stack.
     * 
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator minus() {
    	operation(Operation.SUBTRACT);
    	return this;
    }

    /**
     * Multiply two numbers and put the result on the stack.
     * 
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator multiply() {
    	operation(Operation.MULTIPLY);
    	return this;
    }

    /**
     * Divide two numbers (integer division) and put the result on the stack.
     * 
     * @return	{@link StackCalculator} instance.
     */
    public StackCalculator divide() {
    	operation(Operation.DIVIDE);
    	return this;
    }
    
    /**
     * Do the operation based on provided {@link Operation} argument.
     * 
     * @param operation	Operation you want to perform.
     */
	private void operation(Operation operation) {
		if (size() < 2) {
			throw new IllegalStateException(
					"Not enough operands; expected 2, got " + size());
		}

		Integer a = stack.pop();
		Integer b = stack.pop();

		if (operation == Operation.ADDITION) {
			stack.push(a + b);
		} else if (operation == Operation.SUBTRACT) {
			stack.push(a - b);
		} else if (operation == Operation.MULTIPLY) {
			stack.push(a * b);
		} else if (operation == Operation.DIVIDE) {
			if(b == 0) throw new IllegalArgumentException("You cannot divide by zero!");
			stack.push(a / b);
		}
	}

	/**
	 * @return	Returns the result (top of the stack).
	 */
    public int result() {
        return size() > 0 ? stack.peek() : 0;
    }

    /**
     * @return Returns the stack size.
     */
    public int size() {
        return stack.size();
    }

}
