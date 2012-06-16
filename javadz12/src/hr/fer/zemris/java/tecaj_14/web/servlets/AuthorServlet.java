package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] params = req.getPathInfo().substring(1).split("/");
		
		if(params.length == 1) {
			// Get author
			BlogUser user = new BlogUser();
			user.setNick(params[0]);
			
			BlogUser author = DAOProvider.getDAO().read(user);
			if(author == null) {
				// Error
				req.setAttribute("errorMessage", "Invalid author NICK '" + user.getNick() + "'.");
				req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
				return;
			}
			
			req.setAttribute("titles", DAOProvider.getDAO().getBlogEntries(author));
			req.setAttribute("authorNick", author.getNick());
			req.setAttribute("pageTitle", "Author | " + author.getFirstName() + " " + author.getLastName());
			req.getRequestDispatcher("/WEB-INF/pages/Author.jsp").forward(req, resp);
		} else if (params.length == 2) {
			// Blog entry page or new/edit
			
			
			
		} else {
			// Error
			req.setAttribute("errorMessage", "Invalid URL. Supported '/author/NICK', '/author/NICK/new', '/author/NICK/edit' or '/author/NICK/EID'.");
			req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
	}
	
}