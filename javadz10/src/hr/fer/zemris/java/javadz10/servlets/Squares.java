package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates table of number squares from the
 * provided parameters (a - starting number, b - 
 * ending number).
 */
public class Squares extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String parameterA = req.getParameter("a");
		String parameterB = req.getParameter("b");
		
		int a = 0;
		int b = 20;
		
		try {
			a = Integer.parseInt(parameterA);
		} catch(NumberFormatException ignorable) {}
		
		try {
			b = Integer.parseInt(parameterB);
		} catch(NumberFormatException ignorable) {}
		
		if(a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		if(b > (a+20)) {
			b = a+20;
		}
		
		req.setAttribute("parameterA", a);
		req.setAttribute("parameterB", b);
		
		req.getRequestDispatcher("/WEB-INF/pages/squares.jsp").forward(req, resp);
	}
	
}
