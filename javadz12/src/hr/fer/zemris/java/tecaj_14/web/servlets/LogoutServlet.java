package hr.fer.zemris.java.tecaj_14.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *	Removes session data for the user and therefore it's not
 * 	longer logged in.
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().removeAttribute("current.user.id");
		req.getSession().removeAttribute("current.user.fn");
		req.getSession().removeAttribute("current.user.ln");
		req.getSession().removeAttribute("current.user.nick");
		
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
	
}