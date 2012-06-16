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
			System.out.println("Trying to insert new user...");
			
			BlogUser user = new BlogUser();
			registerForm.toDomainObject(user);
			
			DAOProvider.getDAO().update(user);
			
			req.setAttribute("pageTitle", "Success!");
			req.getRequestDispatcher("/WEB-INF/pages/RegisterSuccess.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("model.object", registerForm);
		req.setAttribute("pageTitle", "Register");
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
		
	}
	
}