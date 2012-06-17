package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import hr.fer.zemris.java.tecaj_14.webforms.RegisterForm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *	Page which displays registration form and have implementation
 *	for registration (inserting new users in the database). 
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RegisterForm registerForm = new RegisterForm();
		BlogUser user = new BlogUser();
		registerForm.fromDomainObject(user);
		
		req.setAttribute("model.object", registerForm);
		req.setAttribute("pageTitle", "Register");
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RegisterForm registerForm = new RegisterForm();
		registerForm.fill(req);
		registerForm.checkErrors();
		
		if(!registerForm.hasError()) {
			BlogUser user = new BlogUser();
			registerForm.toDomainObject(user);
			
			BlogUser existingUser = DAOProvider.getDAO().getUser(user.getNick());
			if(existingUser != null) {
				registerForm.getErrors().put("nick", "Please choose some other nickname, '" + user.getNick() + "' is already taken :(");
			} else {
				DAOProvider.getDAO().createOrUpdate(user);
				
				req.setAttribute("pageTitle", "Success!");
				req.getRequestDispatcher("/WEB-INF/pages/RegisterSuccess.jsp").forward(req, resp);
				return;
			}
		}
		
		req.setAttribute("model.object", registerForm);
		req.setAttribute("pageTitle", "Register");
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
		
	}
	
}