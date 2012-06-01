package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Generates OS usage chart via {@link JFreeChart} library.
 */
public class ReportImage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, "OS Usage");
        
        // Convert to an image and encode it
        byte[] image = ChartUtilities.encodeAsPNG(chart.createBufferedImage(500, 300));
        resp.getOutputStream().write(image);
	}
	
	/**
	 * Creates new {@link JFreeChart} chart from {@link PieDataset} dataset.
	 * 
	 * @param dataset	Dataset you want to use.
	 * @param title		Title of your chart.
	 * @return			Generated chart object.
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(
				title, // chart title
				dataset, // data
				true, // include legend
				true, // enable tooltips
				false // no urls
		);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	/**
	 * Creates dataset used for our chart.
	 * 
	 * @return	Chart dataset.
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);
		return result;

	}

}