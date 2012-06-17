package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import hr.fer.zemris.java.tecaj_14.webforms.EntryForm;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Page to display author info as well as create or update existing
 * blog entries by this author. Actions are allowed only if logged in
 * user is matching provided author in the URL. 
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] params = req.getPathInfo().substring(1).split("/");
		
		if(params.length == 1 || params.length == 2) {
			// Get author
			BlogUser user = new BlogUser();
			user.setNick(params[0]);
			
			BlogUser author = DAOProvider.getDAO().getUser(user.getNick());
			if(author == null) {
				// Error
				showError(req, resp, "Invalid author NICK '" + user.getNick() + "'.");
				return;
			}
			
			// Display author info
			if(params.length == 1) {
				req.setAttribute("titles", DAOProvider.getDAO().getBlogEntries(author));
				req.setAttribute("authorNick", author.getNick());
				req.setAttribute("pageTitle", "Author | " + author.getFirstName() + " " + author.getLastName());
				req.getRequestDispatcher("/WEB-INF/pages/Author.jsp").forward(req, resp);
				return;
			}

			if(params.length == 2) {
				// Check if logged in user is the same as the requested one (in URL)
				if(params[1].equals("new") || params[1].equals("edit")) {
					String loggedInNick = (String) req.getSession().getAttribute("current.user.nick");
					if(loggedInNick == null) {
						showError(req, resp, "Please <a href=\"" + req.getContextPath() + "/servleti/main\">log in</a> first.");
						return;
					}
					
					if(!loggedInNick.equals(author.getNick())) {
						showError(req, resp, "You cannot add or edit under someone elses nick/account.");
						return;
					}
				}
				
				if(params[1].equals("new")) {
					newBlogEntryForm(req, resp);
				} else if(params[1].equals("edit")) {
					editBlogEntryForm(req, resp);
				} else {
					displaySingleEntry(req, resp, params, author);
				}
			}
		} else {
			// Error - invalid URL
			showError(req, resp, "Invalid URL. Supported '/author/NICK', '/author/NICK/new', '/author/NICK/edit' or '/author/NICK/EID'.");
			return;
		}
	}
	
	/**
	 * Displays the form for the new blog entry.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void newBlogEntryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// New entry
		EntryForm entryForm = new EntryForm();
		BlogEntry entry = new BlogEntry();
		entryForm.fromDomainObject(entry);
		
		req.setAttribute("model.object", entryForm);
		req.setAttribute("pageTitle", "New blog entry");
		req.getRequestDispatcher("/WEB-INF/pages/NewEditEntry.jsp").forward(req, resp);
	}
	
	/**
	 * Displays the form for editing existing blog entry.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void editBlogEntryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Edit entry with parameter ID
		String idParam = req.getParameter("id");
		if(idParam == null) {
			// No entry ID provided
			showError(req, resp, "Please provide entry ID with paramter 'id' in the URL.");
			return;
		}
		
		try {
			Integer.parseInt(idParam);
		} catch(NumberFormatException e) {
			// ID is not a number
			showError(req, resp, "Provided ID '" + idParam + "' is not an integer.");
			return;
		}
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(idParam));
		EntryForm entryForm = new EntryForm();
		entryForm.fromDomainObject(entry);
		
		req.setAttribute("model.object", entryForm);
		req.setAttribute("pageTitle", "Edit blog entry with ID=" + idParam);
		req.getRequestDispatcher("/WEB-INF/pages/NewEditEntry.jsp").forward(req, resp);
	}
	
	/**
	 * Displays the single blog entry - info & comments.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @param author				Entry author (creator).
	 * @param params				URL parameters.
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void displaySingleEntry(HttpServletRequest req, HttpServletResponse resp, String[] params, BlogUser author) throws ServletException, IOException {
		// Display single entry
		String entryId = params[1];
		
		int id = 0;
		try {
			id = Integer.parseInt(entryId);
		} catch(NumberFormatException e) {
			// ID is not a number
			showError(req, resp, "Provided entry ID '" + entryId + "' is not an integer.");
			return;
		}
		
		BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
		if(blogEntry == null) {
			showError(req, resp, "Blog entry with ID '" + entryId + "' under nick '" + author.getNick() + "' does not exist!");
			return;
		}
		
		req.setAttribute("pageTitle", blogEntry.getTitle());
		req.setAttribute("blog.entry", blogEntry);
		req.getRequestDispatcher("/WEB-INF/pages/BlogEntry.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(req.getParameter("comment") != null) {
			postNewComment(req, resp);
			return;
		}
		
		EntryForm entryForm = new EntryForm();
		entryForm.fill(req);
		entryForm.checkErrors();
		
		if(!entryForm.hasError() || (entryForm.getErrors().size() == 1 && entryForm.hasErrorFor("id"))) {
			createOrUpdateBlogEntry(req, resp, entryForm);
			return;
		}
		
		req.setAttribute("model.object", entryForm);
		req.setAttribute("pageTitle", "New blog entry");
		req.getRequestDispatcher("/WEB-INF/pages/NewEditEntry.jsp").forward(req, resp);
	}
	
	/**
	 * Creates or updates existing blog entry.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @param entryForm				EntryForm which holds entry data.
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void createOrUpdateBlogEntry(HttpServletRequest req, HttpServletResponse resp, EntryForm entryForm) throws ServletException, IOException {
		BlogUser user = new BlogUser();
		user.setNick((String) req.getSession().getAttribute("current.user.nick"));
		user = DAOProvider.getDAO().getUser(user.getNick());
		
		BlogEntry entry = new BlogEntry();
		
		if(entry.getId() == null) {
			entry.setCreatedAt(new Date());
			entry.setCreator(user);
		} else {
			entry = DAOProvider.getDAO().getBlogEntry(entry.getId());
		}
		
		entryForm.toDomainObject(entry);
		
		entry.setLastModifiedAt(new Date());
		DAOProvider.getDAO().createOrUpdate(entry);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + entry.getId());
	}
	
	/**
	 * Posts new comment in the database.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void postNewComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] params = req.getPathInfo().substring(1).split("/");
		
		if(req.getParameter("comment").trim().length() == 0) {
			showError(req, resp, "You cannot post empty comment! Please go <a href=\"" + req.getContextPath() + "/servleti/author/" + params[0] + "/" + params[1] + "\">back</a> and type in something...");
			return;
		}
		
		BlogUser user = new BlogUser();
		user.setNick((String) req.getSession().getAttribute("current.user.nick"));
		user = DAOProvider.getDAO().getUser(user.getNick());
		
		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setId(Long.parseLong(params[1]));
		blogEntry = DAOProvider.getDAO().getBlogEntry(blogEntry.getId());
		
		BlogComment comment = new BlogComment();
		comment.setMessage(req.getParameter("comment"));
		comment.setBlogEntry(blogEntry);
		comment.setUsersEMail(user.getNick());
		comment.setPostedOn(new Date());
		
		DAOProvider.getDAO().createOrUpdate(comment);
		
		req.setAttribute("pageTitle", blogEntry.getTitle());
		req.setAttribute("blog.entry", blogEntry);
		req.getRequestDispatcher("/WEB-INF/pages/BlogEntry.jsp").forward(req, resp);
	}
	
	/**
	 * Display the error page with appropriate message.
	 * 
	 * @param req					HttpServletRequest
	 * @param resp					HttpServletResponse
	 * @param message				Error message you want to display.
	 * @throws ServletException		ServletException
	 * @throws IOException			IOException
	 */
	private void showError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
		// No entry ID provided
		req.setAttribute("errorMessage", message);
		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
		return;
	}
	
}