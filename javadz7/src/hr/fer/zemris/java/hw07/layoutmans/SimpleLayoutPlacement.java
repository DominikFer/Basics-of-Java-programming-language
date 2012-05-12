package hr.fer.zemris.java.hw07.layoutmans;

/**
 * Enumeration class for simple layout constraints.
 */
public enum SimpleLayoutPlacement {

	LEFT("LEFT"),
	CENTER("CENTER");
	
	private final String text;
	
    private SimpleLayoutPlacement(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
	
}
