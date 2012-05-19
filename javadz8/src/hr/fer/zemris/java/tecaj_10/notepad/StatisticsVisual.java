package hr.fer.zemris.java.tecaj_10.notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatisticsVisual extends JComponent {

	private static final long serialVersionUID = 1681108295371279764L;
	
	final private int rows = 6;
	
	final private int fontSize = 10;
	final private int xAxisHeight = 20;
	final private int yAxisWidth = 30;
	final private Insets yAxisPadding = new Insets(0, 0, 2, 0);
	final private Insets xAxisPadding = new Insets(0, 2, 0, 0);
	final private Color background = new Color(255, 255, 255);
	final private Color lineColor = new Color(181, 181, 181);
	final private Color columnColor = new Color(0, 69, 135);
	
	private int yMax = 1;
	
	private JComponent graph;
	
	private JLabel title;
	private int[] stats;
	private JLabel[] dataOrigins;
	
	public StatisticsVisual(JLabel title, JLabel[] stats) {
		this.title = title;
		this.stats = new int[stats.length];
		this.dataOrigins = stats;
		
		pullData();
		initComponents();
	}
	
	public void refresh() {
		pullData();
		graph.repaint();
	}
	
	private void pullData() {
		yMax = 1;
		for (int i = 0; i < dataOrigins.length; ++i) {
			this.stats[i] = Integer.parseInt(dataOrigins[i].getText());
			yMax = Math.max(yMax, this.stats[i]);
		}
	}
		
	private void initComponents() {
		
		setLayout(new BorderLayout());
		add(title, BorderLayout.PAGE_START);
		
		graph = getGraphPane();
		add(graph, BorderLayout.CENTER);
		
	}
	
	private JComponent getGraphPane() {
				
		JComponent canvas = new JGraph();
		
		canvas.setBackground(background);
		canvas.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		return canvas;
		
	}
	
	private class JGraph extends JPanel {
		
		private static final long serialVersionUID = 9069699967692332766L;
		
		private JPanel xAxis;
		
		public JGraph() {
			setLayout(null);
			
			xAxis = new JPanel(null);
			xAxis.setBackground(background);
			add(xAxis);
			setXAxis();
		
		}
		
		private void setXAxis() {
			
			xAxis.setBounds(getGraphX1() + xAxisPadding.left, getGraphY2(), getDrawableWidth(), 100);
			xAxis.removeAll();
			
			int colWidth = (int) (getDrawableWidth() / (double) stats.length);
			for (int i = 0; i < stats.length; ++i) {
				JLabel currLabel = new JLabel(new Integer(i+1).toString(), JLabel.CENTER);
				currLabel.setBounds(i*colWidth, 0, colWidth, xAxisHeight);
				currLabel.setFont(new Font(currLabel.getFont().getFontName(),
						currLabel.getFont().getStyle(), fontSize));
				xAxis.add(currLabel);
			}
			xAxis.revalidate();
			
		}

		private int getDrawableHeight() {
			Insets insets = getInsets();
			return getHeight() - (yAxisPadding.top + yAxisPadding.bottom) - insets.top - insets.bottom - xAxisHeight;
		}
		
		private int getDrawableWidth() {
			Insets insets = getInsets();
			return getWidth() - (xAxisPadding.left + xAxisPadding.right) - insets.left - insets.right - yAxisWidth;
		}
		
		private int getGraphX1() {
			return yAxisWidth + getInsets().left;
		}
		
		private int getGraphX2() {
			return getWidth() - getInsets().right;
		}
		
		private int getGraphY1() {
			return 0 + getInsets().top;
		}
		
		private int getGraphY2() {
			return getHeight() - xAxisHeight - getInsets().bottom;
		}
		
		private void drawHorizontalLines(Graphics g) {
			
			g.setColor(lineColor);
			double drawableHeight = getDrawableHeight();
			double rowHeight = drawableHeight / rows;
			int topOffset = getInsets().top;
			int leftOffset = getGraphX1();
			for (int i = 0; i <= rows; ++i) {
				g.drawLine(leftOffset, (int)(topOffset + i*rowHeight),
						getGraphX2(), (int)(topOffset + i*rowHeight));
			}
			
		}
		
		private void drawVerticalLines(Graphics g) {
							
			g.setColor(lineColor);
			
			// left line
			g.drawLine(getGraphX1() + xAxisPadding.left, getGraphY1(),
					getGraphX1()+ xAxisPadding.left, getGraphY2());
			
			// right line
			g.drawLine(getGraphX2(), getGraphY1(),
					getGraphX2(), getGraphY2());
			
			// small lines around numbers
			double drawableWidth = getDrawableWidth();
			int colWidth = (int) (drawableWidth / (double)stats.length);
			for (int i = 0; i < stats.length; ++i) {
				g.drawLine(getGraphX1() + xAxisPadding.left + i*colWidth, getGraphY2()-yAxisPadding.bottom,
						getGraphX1() + xAxisPadding.left + i*colWidth, getGraphY2());
			}
			
		}
		
		private void drawColumns(Graphics g) {
			
			double drawableWidth = getDrawableWidth();
			double drawableHeight = getDrawableHeight();
			
			double columnSpace = drawableWidth / stats.length;
			g.setColor(columnColor);
			for (int i = 0; i < stats.length; ++i) {
				int width = (int)(columnSpace * 0.5);
				int height = (int)(stats[i] / (double)yMax * drawableHeight) - 1;
				int x = (int)(getGraphX1() + xAxisPadding.left + i*columnSpace + (columnSpace - width)/2);
				int y = getGraphY2() - yAxisPadding.bottom - height;
				g.fillRect(x, y, width, height);
			}
			
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(background);
			g.fillRect(0, 0, getWidth(), getHeight());
			setXAxis();
			super.paint(g);
			drawHorizontalLines(g);
			drawVerticalLines(g);
			drawColumns(g);
		}
		
	}
	
	@Override
	public Dimension getMinimumSize() {
		Dimension minimum = new Dimension(
			stats.length*30 + 40, 
			rows * 30 + xAxisHeight
		);
		return minimum;
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension preffered = new Dimension(
			stats.length*30 + 40, 
			rows * 30 + xAxisHeight
		);
		return preffered;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(background);
		super.paint(g);
	}

}
