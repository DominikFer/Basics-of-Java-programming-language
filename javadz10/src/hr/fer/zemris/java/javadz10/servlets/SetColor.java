package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the picked background color for all pages on this
 * application.
 */
public class SetColor extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");
		req.getSession().setAttribute("pickedBgCol", color);
		
		req.getRequestDispatcher("/WEB-INF/pages/colors.jsp").forward(req, resp);
	}
	
}
