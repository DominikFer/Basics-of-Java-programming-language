package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import hr.fer.zemris.java.tecaj_14.webforms.LoginForm;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		LoginForm loginForm = new LoginForm();
		BlogUser user = new BlogUser();
		loginForm.fromDomainObject(user);
		
		rendredPage(req, resp, loginForm);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginForm loginForm = new LoginForm();
		loginForm.fill(req);
		loginForm.checkErrors();
		
		if(!loginForm.hasError()) {
			BlogUser user = new BlogUser();
			loginForm.toDomainObject(user);
			
			BlogUser loggedInUser = DAOProvider.getDAO().read(user);
			if(loggedInUser != null) {
				req.getSession().setAttribute("current.user.id", loggedInUser.getId());
				req.getSession().setAttribute("current.user.fn", loggedInUser.getFirstName());
				req.getSession().setAttribute("current.user.ln", loggedInUser.getLastName());
				req.getSession().setAttribute("current.user.nick", loggedInUser.getNick());
			} else {
				req.setAttribute("wrong.nickname.or.password", "You provided wrong nickname or password. Please try again.");
			}
		}
		
		rendredPage(req, resp, loginForm);
	}
	
	private void rendredPage(HttpServletRequest req, HttpServletResponse resp, LoginForm loginForm) throws ServletException, IOException {
		List<BlogUser> authors = DAOProvider.getDAO().readAllUsers();
		req.setAttribute("authors", authors);
		
		req.setAttribute("model.object", loginForm);
		req.setAttribute("pageTitle", "Homepage");
		req.getRequestDispatcher("/WEB-INF/pages/Homepage.jsp").forward(req, resp);
	}
	
}