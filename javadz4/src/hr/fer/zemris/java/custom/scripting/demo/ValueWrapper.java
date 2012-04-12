package hr.fer.zemris.java.custom.scripting.demo;

/**
 * <p>Class which stores {@link Object} value and performs particular
 * operations on it depending if it's <code>null</code>, {@link Integer} or
 * {@link Double}.</p>
 * <p>If the value is <code>null</code> it behaves (if you are doing some
 * operations with it) like an {@link Integer} with value <code>0</code>.</p>
 */
public class ValueWrapper {

	private static final int OPERATION_INCREMENT = 1;
	private static final int OPERATION_DECREMENT = 2;
	private static final int OPERATION_MULTIPLY = 3;
	private static final int OPERATION_DIVIDE = 4;

	private static final int TYPE_INTEGER = 1;
	private static final int TYPE_DOUBLE = 2;
	
	private Object value;
	
	/**
	 * Creates the {@link ValueWrapper} object with
	 * initial <code>value</code>.
	 * 
	 * @param value	Initial value you want to set.
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * @return Returns the object value.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Sets the object value.
	 * 
	 * @param value	Value you want to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Increments the current value for the value of <code>incValue</code>.
	 * 
	 * @param incValue	Value for which you want to increment.
	 */
	public void increment(Object incValue) {
		performOperation(incValue, OPERATION_INCREMENT);
	}
	
	/**
	 * Decrements the current value for the value of <code>incValue</code>.
	 * 
	 * @param decValue	Value for which you want to decrement.
	 */
	public void decrement(Object decValue) {
		performOperation(decValue, OPERATION_DECREMENT);
	}

	/**
	 * Multiplies the current value for the value of <code>incValue</code>.
	 * 
	 * @param mulValue	Value for which you want to multiply.
	 */
	public void multiply(Object mulValue) {
		performOperation(mulValue, OPERATION_MULTIPLY);
	}

	/**
	 * Divides the current value for the value of <code>incValue</code>.
	 * 
	 * @param divValue	Value for which you want to divide.
	 * @throws IllegalArgumentException if the <code>divValue</code>
	 * is equal to <code>0</code>.
	 */
	public void divide(Object divValue) {
		performOperation(divValue, OPERATION_DIVIDE);
	}
	
	/**
	 * Performs the mathematical operation based on <code>operationType</code>.
	 * 
	 * @param value			Second value for operation (first one is the value in
	 * 						{@link ValueWrapper} itself.
	 * @param operationType	Operation you want to perform.
	 */
	private void performOperation(Object value, int operationType) {
		if(getType(this.value) == TYPE_INTEGER && getType(value) == TYPE_INTEGER) {
			this.value = calculate(
							this.value == null ? 0 : Integer.parseInt(this.value.toString()),
							value == null ? 0 : Integer.parseInt(value.toString()),
							operationType
						 );
		} else {
			this.value = calculate(
							this.value == null ? 0 : Double.parseDouble(this.value.toString()),
							value == null ? 0 : Double.parseDouble(value.toString()),
							operationType
						 );
		}
	}
	
	/**
	 * Calculates operation based on <code>operationType</code> between two
	 * {@link Integer} values.
	 * 
	 * @param first			First value.
	 * @param second		Second value.
	 * @param operationType	Operation you want to perform.
	 * @return				{@link Integer} result.
	 */
	private int calculate(int first, int second, int operationType) {
		switch (operationType) {
			case OPERATION_INCREMENT: return first + second;
			case OPERATION_DECREMENT: return first - second;
			case OPERATION_MULTIPLY: return first * second;
			case OPERATION_DIVIDE: 
				if(second == 0) throw new IllegalArgumentException("You cannot divide by zero!");
				return first / second;
			default: return 0;
		}
	}

	/**
	 * Calculates operation based on <code>operationType</code> between two
	 * {@link Double} values.
	 * 
	 * @param first			First value.
	 * @param second		Second value.
	 * @param operationType	Operation you want to perform.
	 * @return				{@link Double} result.
	 */
	private double calculate(double first, double second, int operationType) {
		switch (operationType) {
			case OPERATION_INCREMENT: return first + second;
			case OPERATION_DECREMENT: return first - second;
			case OPERATION_MULTIPLY: return first * second;
			case OPERATION_DIVIDE:
				if(Double.valueOf(second).compareTo(0.0) == 0) throw new IllegalArgumentException("You cannot divide by zero!");
				return first / second;
			default: return 0.0;
		}
	}
	
	/**
	 * Returns the object type.
	 * 
	 * @param value	Object for which you want retrieve the type.
	 * @return		<code>1</code> if <code>null</code> or
	 * {@link Integer}, <code>2</code> if it's {@link Double}, else throws exception.
	 * @throws RuntimeException If the object is not <code>null</code>, {@link Integer} or {@link Double}.
	 */
	private int getType(Object value) {
		if(value == null) return TYPE_INTEGER;
		if(isInteger(value.toString())) return TYPE_INTEGER;
		if(isDouble(value.toString())) return TYPE_DOUBLE;
		
		throw new RuntimeException();
	}

	/**
	 * Checks if the value is {@link Double} or not.
	 * 
	 * @param value	Value you want to check.
	 * @return		<code>true</code> if it's {@link Double}, <code>false</code> otherwise.
	 */
	private boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the value is {@link Integer} or not.
	 * 
	 * @param value	Value you want to check.
	 * @return		<code>true</code> if it's {@link Integer}, <code>false</code> otherwise.
	 */
	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Compares the two values, currently stored value and <code>withValue</code>.
	 * 
	 * @param withValue	Value you want compare to.
	 * @return			<p><code>0</code> if values are equal or both <code>null</code>.</p>
	 * 					<p><code>-1</code> if the currently stored value is smaller then argument.</p>
	 * 					<p><code>1</code> if the currently stored value is larger then argument.</p>
	 */
	public int numCompare(Object withValue) {
		if(this.value == null && withValue == null) return 0;
		
		if(getType(this.value) == TYPE_INTEGER && getType(withValue) == TYPE_INTEGER) {
			Integer first = Integer.valueOf(this.value == null ? 0 : Integer.parseInt(this.value.toString()));
			Integer second = Integer.valueOf(withValue == null ? 0 : Integer.parseInt(withValue.toString()));
			return first.compareTo(second);
		} else {
			Double first = Double.valueOf(this.value == null ? 0 : Double.parseDouble(this.value.toString()));
			Double second = Double.valueOf(withValue == null ? 0 : Double.parseDouble(withValue.toString()));
			return first.compareTo(second);
		}
	}
}
