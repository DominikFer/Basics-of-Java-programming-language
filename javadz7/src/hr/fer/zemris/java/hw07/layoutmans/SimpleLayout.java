package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * <p>Simple layout manager with only two possible constraints -
 * LEFT and CENTER defined in {@link SimpleLayoutPlacement}. <code>LEFT</code>
 * component will always be places on the left side of the container,
 * if component is RIGHT_TO_LEFT it will be places on the right side of the
 * container. Component added with <code>CENTER</code> constraint will
 * fill entire remaining space.</p>
 */
public class SimpleLayout implements LayoutManager2 {
	
	private static final int SIZE_MIN = 1;
	private static final int SIZE_MAX = 2;
	private static final int SIZE_PREFFERED = 3;
	
	private Component left;
	private Component center;
	
	@Override
	public void addLayoutComponent(Component component, Object constraints) {
		if ((constraints == null) || (constraints instanceof SimpleLayoutPlacement)) {
			addLayoutComponent(constraints.toString(), component);
		} else {
			throw new IllegalArgumentException("Cannot add component '" + component + "' to the layout because of the unknown constrain " + constraints);
		}
	}
	
	@Override
	public void layoutContainer(Container target) {
		Insets insets = target.getInsets();
		int top = insets.top;
		int bottom = target.getHeight() - insets.bottom;
		int left = insets.left;
		int right = target.getWidth() - insets.right;
		
		Component component = null;
		
		if ((component = getChild(SimpleLayoutPlacement.LEFT)) != null) {
			component.setSize(component.getWidth(), bottom - top);
			Dimension d = component.getPreferredSize();
			
			if(target.getComponentOrientation().isLeftToRight()) {
				component.setBounds(right - d.width, top, d.width, bottom - top);
				right -= d.width;
			} else {
				component.setBounds(left, top, d.width, bottom - top);
				left += d.width;
			}
		}
		
		if ((component = getChild(SimpleLayoutPlacement.CENTER)) != null) {
			component.setBounds(left, top, right - left, bottom - top);
		}
	}
	
	/**
	 * Returns the child based on the {@link SimpleLayoutPlacement}.
	 * 
	 * @param layoutDirection	Direction of the requested component.
	 * @return					Component with the provided {@link SimpleLayoutPlacement}.
	 */
	private Component getChild(SimpleLayoutPlacement layoutDirection) {
		Component result = null;
		
		if (layoutDirection == SimpleLayoutPlacement.LEFT) {
			result = left;
		} else if (layoutDirection == SimpleLayoutPlacement.CENTER) {
			result = center;
		}
		
		if (result != null && !result.isVisible()) {
			result = null;
		}
		
		return result;
	}
	
	/**
	 * Adds component to the manager.
	 * 
	 * @throws IllegalArgumentException if you try to remove a add a component with unkown name (direction).
	 */
	@Override
	public void addLayoutComponent(String componentDirection, Component component) {
		if (componentDirection == null) {
			componentDirection = SimpleLayoutPlacement.CENTER.toString();
		}
		
		if (SimpleLayoutPlacement.CENTER.toString().equals(componentDirection)) {
			center = component;
		} else if (SimpleLayoutPlacement.LEFT.toString().equals(componentDirection)) {
			left = component;
		} else {
			throw new IllegalArgumentException("Cannot add component '" + component + "' to the layout because of the unknown component name (direction) " + componentDirection);
		}
	}
	
	/**
	 * Removes layout component from the manager.
	 * 
	 * @throws IllegalArgumentException if you try to remove a component which does not exist.
	 */
	public void removeLayoutComponent(Component component) {
		if (component == center) {
			center = null;
		} else if (component == left) {
			left = null;
		} else {
			throw new IllegalArgumentException("Cannot remove component '" + component + "' from layout because it does not exist.");
		}
	}
	
	/**
     * Returns the minimum layout size.
     */
	public Dimension minimumLayoutSize(Container target) {
		return getSize(target, SIZE_MIN);
    }

	/**
     * Returns the preferred layout size.
     */
    public Dimension preferredLayoutSize(Container target) {
    	return getSize(target, SIZE_PREFFERED);
    }

    /**
     * Returns the maximum layout size.
     */
    public Dimension maximumLayoutSize(Container target) {
    	return getSize(target, SIZE_MAX);
    }
    
	/**
	 * Returns the size of the <code>container</code> based on <code>sizeType</code>.
	 * 
	 * @param target		Container for which you would like to know its size.
	 * @param sizeType		Which size you would like? <code>SIZE_MIN</code>,
	 * 						<code>SIZE_PREFFERED</code> or <code>SIZE_MAX</code>. 
	 * @return				{@link Dimension} size of the provided container.
	 */	
 	private Dimension getSize(Container target, int sizeType) {
    	Dimension size = new Dimension(0, 0);
    	
    	Component component = null;
    	
    	if ((component = getChild(SimpleLayoutPlacement.LEFT)) != null) {
    		Dimension dimension = getComponentSize(component, sizeType);
    		size.width += dimension.width;
    		size.height = Math.max(dimension.height, size.height);
    	}
    	
    	if ((component = getChild(SimpleLayoutPlacement.CENTER)) != null) {
    		Dimension dimension = getComponentSize(component, sizeType);
    		size.width += dimension.width;
    		size.height = Math.max(dimension.height, size.height);
    	}
    	
    	Insets insets = target.getInsets();
    	size.width += insets.left + insets.right;
    	size.height += insets.top + insets.bottom;
    	
    	return size;
 	}
 	
 	/**
 	 * Returns the size of the <code>component</code> based on <code>sizeType</code>.
 	 * 
 	 * @param component		Component for which you would like to know its size.
 	 * @param sizeType		Which size you would like? <code>SIZE_MIN</code> or
	 * 						<code>SIZE_PREFFERED</code> or <code>SIZE_MAX</code>. 
 	 * @return				{@link Dimension} size of the provided component.
 	 */
 	private Dimension getComponentSize(Component component, int sizeType) {
 		if(sizeType == SIZE_MIN) return component.getMinimumSize();
 		if(sizeType == SIZE_PREFFERED) return component.getPreferredSize();
 		
 		return component.getMaximumSize();
	}

 	/**
 	 * Not used in this class.
 	 */
 	public float getLayoutAlignmentX(Container parent) { return 0.5f; }
 	
	/**
 	 * Not used in this class.
 	 */
 	public float getLayoutAlignmentY(Container parent) { return 0.5f; }
 	
    /**
 	 * Not used in this class.
 	 */
    public void invalidateLayout(Container target) {}
}