package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import javax.swing.SizeRequirements;

/**
 * <p>Layout manager which places the components along the vertical axis; it
 * does not respect their preferred width when doing layout but instead
 * it always stretches the components to fill the horizontal area.</p>
 * <p>Layout directions are defined by {@link StackedLayoutDirection} enumerations.
 * If <code>FROM_TOP</code> direction is used, components are placed from top of
 * the container. If <code>FROM_BOTTOM</code> direction is used, components are
 * placed from bottom of the container. the container. If <code>FILL</code>
 * direction is used, components are stretched so that they fill entire container.

 */
public class StackedLayout implements LayoutManager2 {

	private static final int SIZE_MIN = 1;
	private static final int SIZE_MAX = 2;
	private static final int SIZE_PREFFERED = 3;

	/** Layout manager direction */
	private StackedLayoutDirection layoutDirection;
	
	private transient SizeRequirements[] xChildren;
	private transient SizeRequirements xTotal;
	private transient SizeRequirements[] yChildren;
	private transient SizeRequirements yTotal;
	
	/**
	 * Constructor.
	 * 
	 * @param layoutDirection	Direction which will be used for this layout.
	 */
	public StackedLayout(StackedLayoutDirection layoutDirection) {
		this.layoutDirection = layoutDirection;
	}

	@Override
	public void layoutContainer(Container target) {
		int nChildren = target.getComponentCount();
		int[] xOffsets = new int[nChildren];
		int[] xSpans = new int[nChildren];
		int[] yOffsets = new int[nChildren];
		int[] ySpans = new int[nChildren];
		
		Dimension alloc = target.getSize();
		Insets in = target.getInsets();
		alloc.width -= in.left + in.right;
		alloc.height -= in.top + in.bottom;
		
		checkRequests(target);
		
		SizeRequirements.calculateAlignedPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans, true);
		SizeRequirements.calculateTiledPositions(alloc.height, yTotal, yChildren, yOffsets, ySpans);
		
		if(this.layoutDirection == StackedLayoutDirection.FROM_TOP || this.layoutDirection == StackedLayoutDirection.FILL) {
			for(int i = 0; i < nChildren; i++) {
				Component c = target.getComponent(i);
				c.setBounds((int) Math.min((long) in.left, Integer.MAX_VALUE), (int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE), xSpans[i], ySpans[i]);
			}
		} else {
			int fromBottomYPositon = alloc.height - yOffsets[nChildren-1];
			for(int i = 0; i < nChildren; i++) {
				Component c = target.getComponent(i);
				c.setBounds((int) Math.min((long) in.left, Integer.MAX_VALUE), (int) Math.min(fromBottomYPositon, Integer.MAX_VALUE), xSpans[i], ySpans[i]);
				fromBottomYPositon += ySpans[i];
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		return getSize(target, SIZE_MIN);
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		return getSize(target, SIZE_PREFFERED);
	}

	@Override
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
		checkRequests(target);
		
		Dimension size = null;
		
		switch(sizeType) {
			case SIZE_MIN: size = new Dimension(xTotal.minimum, yTotal.minimum); break;
			case SIZE_PREFFERED: size = new Dimension(xTotal.preferred, yTotal.preferred); break;
			case SIZE_MAX: size = new Dimension(xTotal.maximum, yTotal.maximum); break;
		}
		
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		checkRequests(target);
		return xTotal.alignment;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		checkRequests(target);
		return yTotal.alignment;
	}

	@Override
	public void invalidateLayout(Container target) {
		xChildren = null;
		xTotal = null;
		yChildren = null;
		yTotal = null;
	}
	
	/**
	 * Checks if the layout has been invalidated, if so - refresh the
	 * {@link SizeRequirements} of the each child.
	 * 
	 * @param target	General layout container used to store all the children.
	 */
	private void checkRequests(Container target) {
		if(xChildren == null || yChildren == null) {
			int n = target.getComponentCount();
			xChildren = new SizeRequirements[n];
			yChildren = new SizeRequirements[n];
			for(int i = 0; i < n; i++) {
				Component c = target.getComponent(i);
				if(!c.isVisible()) {
					xChildren[i] = new SizeRequirements(0,0,0,c.getAlignmentX());
					yChildren[i] = new SizeRequirements(0,0,0,c.getAlignmentY());
					continue;
				}
				
				Dimension min = c.getMinimumSize();
				Dimension typ = c.getPreferredSize();
				Dimension max = c.getMaximumSize();
				
				xChildren[i] = new SizeRequirements(min.width, typ.width, max.width, c.getAlignmentX());
				if(this.layoutDirection == StackedLayoutDirection.FILL) {
					yChildren[i] = new SizeRequirements(min.height, typ.height, max.height, c.getAlignmentY());
				} else {
					yChildren[i] = new SizeRequirements(min.height, min.height, min.height, c.getAlignmentY());
				}
			}
			
			xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
			yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
		}
	}

	@Override
	public void removeLayoutComponent(Component target) {}

	@Override
	public void addLayoutComponent(Component target, Object arg1) {}
	
	@Override
	public void addLayoutComponent(String target, Component arg1) {}

}