package hr.fer.zemris.java.custom.scripting.demo;

public class ValueWrapper {

	private static final int OPERATION_INCREMENT = 1;
	private static final int OPERATION_DECREMENT = 2;
	private static final int OPERATION_MULTIPLY = 3;
	private static final int OPERATION_DIVIDE = 4;

	private static final int TYPE_INTEGER = 1;
	private static final int TYPE_DOUBLE = 2;
	
	private Object value;
	
	public ValueWrapper(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void increment(Object incValue) {
		performOperation(incValue, OPERATION_INCREMENT);
	}
	
	public void decrement(Object decValue) {
		performOperation(decValue, OPERATION_DECREMENT);
	}

	public void multiply(Object mulValue) {
		performOperation(mulValue, OPERATION_MULTIPLY);
	}

	public void divide(Object divValue) {
		performOperation(divValue, OPERATION_DIVIDE);
	}
	
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
	
	private int getType(Object value) {
		if(value == null) return TYPE_INTEGER;
		if(isInteger(value.toString())) return TYPE_INTEGER;
		if(isDouble(value.toString())) return TYPE_DOUBLE;
		
		throw new RuntimeException();
	}

	private boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
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
