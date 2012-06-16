package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import hr.fer.zemris.java.tecaj_13.webforms.UnosForm;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to create or update records in the database.
 */
@WebServlet("/servleti/unos/*")
public class UnosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.req = req;
		this.resp = resp;
		
		String[] action = req.getPathInfo().substring(1).split("/");
		
		if(action[0].equals("new")) {
			newRecord();
		} else if (action[0].equals("edit")) {
			editRecord(action);
		} else if (action[0].equals("save")) {
			UnosForm unosForm = new UnosForm();
			unosForm.fill(req);
			unosForm.checkErrors();
			if(unosForm.hasErrorFor("id")) {
				insertNewEntry(unosForm);
			} else {
				updateExistingEntry(unosForm);
			}
		} else {
			// Error
			req.setAttribute("errorMessage", "Available actions are /new, /edit, /save.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
	}
	
	/**
	 * Inserts a new entry into database.
	 * 
	 * @param unosForm				Record details.
	 * @throws ServletException		Servlet exception.
	 * @throws IOException			IO exception.
	 */
	private void insertNewEntry(UnosForm unosForm) throws ServletException, IOException {
		if(unosForm.hasErrorFor("title") || unosForm.hasErrorFor("message") || unosForm.hasErrorFor("EMail")) {
			showJsp(unosForm, "Error");
			return;
		}
		
		Unos unos = new Unos();
		unosForm.toDomainObject(unos);
		unos.setCreatedOn(new Date());
		
		// New entry
		DAOProvider.getDao().save(unos);
		resp.sendRedirect(req.getContextPath() + "/servleti/listajKratko");
	}
	
	/**
	 * Updates existing entry in the database.
	 * 
	 * @param unosForm		Record details.
	 * @throws IOException	IO Exception.
	 */
	private void updateExistingEntry(UnosForm unosForm) throws IOException {
		Unos unos = new Unos();
		unosForm.toDomainObject(unos);
		
		// Update existing entry
		DAOProvider.getDao().update(unos);
		resp.sendRedirect(req.getContextPath() + "/servleti/listajKratko");
	}
	
	/**
	 * Displays a page for a new record.
	 * 
	 * @throws ServletException	ServletException
	 * @throws IOException		IOException
	 */
	private void newRecord() throws ServletException, IOException {
		Unos unos = new Unos();
		UnosForm unosForm = new UnosForm();
		unosForm.fromDomainObject(unos);
		req.setAttribute("model.object", unosForm);
		
		showJsp(unosForm, "New record");
	}

	/**
	 * Displays a page to edit an existing record.
	 * 	
	 * @param recordId				Used to identify record ID.
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException	
	 */
	private void editRecord(String[] recordId) throws ServletException, IOException {
		if(recordId.length < 2) {
			// Error
			req.setAttribute("errorMessage", "You must provide ID to edit some record (for ex. /edit/5).");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		String _id = recordId[1];
		int id = 0;
		try {
			id = Integer.parseInt(_id);
		} catch (NumberFormatException e) {
			// Not proper ID
			req.setAttribute("errorMessage", "ID = '" + id + "' is not an integer.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		Unos unos = null;
		try {
			unos = DAOProvider.getDao().dohvatiUnos(id);
		} catch (DAOException e) {
			// There is no record for that ID
			req.setAttribute("errorMessage", "There is no record with ID = '" + id + "'.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		UnosForm unosForm = new UnosForm();
		unosForm.fromDomainObject(unos);

		showJsp(unosForm, "Edit record");
	}
	
	/**
	 * Displays actual JSP page.
	 * 
	 * @param unosForm				Form you want to show.
	 * @param pageTitle				Page title.
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void showJsp(UnosForm unosForm, String pageTitle) throws ServletException, IOException {
		req.setAttribute("model.object", unosForm);
		req.setAttribute("pageTitle", pageTitle);
		req.getRequestDispatcher("/WEB-INF/pages/unos.jsp").forward(req, resp);
	}
}
