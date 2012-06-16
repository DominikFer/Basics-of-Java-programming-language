package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shows all the details about specific record.
 */
@WebServlet("/servleti/details/*")
public class Details extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String _id = req.getPathInfo().substring(1);
		
		int id = 0;
		try {
			id = Integer.parseInt(_id);
		} catch (NumberFormatException e) {
			// Error
			req.setAttribute("errorMessage", "Invalid ID '" + _id + "'.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		Unos unos = DAOProvider.getDao().dohvatiUnos(id);
		if(unos == null) {
			// Error
			req.setAttribute("errorMessage", "There is no record with ID '" + _id + "'.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("unos", unos);
		req.getRequestDispatcher("/WEB-INF/pages/Details.jsp").forward(req, resp);
	}
}
