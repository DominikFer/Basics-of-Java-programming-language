package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows user to view funny story (with random text color)
 * only if current minute is even number. Error is thrown
 * otherwise.
 */
public class Story extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Random colors
		String[] colors = {
				"aqua",
				"black",
				"fuchsia",
				"maroon",
				"gray",
				"green",
				"red",
				"lime",
				"navy",
				"olive",
				"purple",
				"silver",
				"teal",
				"yellow",
				"white"
		};
		
		int randomColor = new Random().nextInt(colors.length);
		
		req.setAttribute("storyColor", colors[randomColor]);
		req.getRequestDispatcher("/WEB-INF/pages/story.jsp").forward(req, resp);
	}
	
}
