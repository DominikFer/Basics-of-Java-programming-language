<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="hr.fer.zemris.java.tecaj_14.webforms.LoginForm"%>
<%@page import="hr.fer.zemris.java.tecaj_14.web.HTMLSupport"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List" %>
<%
	LoginForm loginForm = (LoginForm) request.getAttribute("model.object");

	String nick = loginForm.getNick() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(loginForm.getNick()) + "\"" : "";
	String password = loginForm.getPassword() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(loginForm.getPassword()) + "\"" : "";
	
	String errors = "";
	if(loginForm.hasError()) {
		errors += "<ul>";
		for(Entry<String, String> error : loginForm.getErrors().entrySet()) {
			errors += "<li>" + error.getValue() + "</li>";
		}
		errors += "</ul>";
	}
	
	boolean loggedIn = false;
	String loggedInUser = "Not logged in";
	if(session.getAttribute("current.user.id") != null) {
		loggedInUser = (String) session.getAttribute("current.user.fn") + " " + (String) session.getAttribute("current.user.ln");
		loggedIn = true;
	}
	
	List<BlogUser> authors = (List<BlogUser>) request.getAttribute("authors");
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>
	
	<h2><%= request.getAttribute("pageTitle") %> | Logiran kao: <%= loggedInUser %><%= loggedIn ? ", <a href=\"" + request.getContextPath() + "/servleti/logout\">Logout</a>" : "" %></h2>
	<h3>Login</h3>
	<form action="<%= request.getContextPath() %>/servleti/main" method="post">
		Nick: <input type="text" id="nick" name="nick" <%= nick %> /><br />
		Password: <input type="password" id="password" name="password" <%= password %> /><br />
		<input type="submit" value="Send" />
	</form>

	<%= request.getAttribute("wrong.nickname.or.password") != null ? request.getAttribute("wrong.nickname.or.password") : "" %>	
	<%= errors %>
	
	<%= loggedIn ? "" : "<p>Click <a href=\"" + request.getContextPath() + "/servleti/register\">here</a> to register.</p>" %>
	
	<ul>
    <% for(BlogUser u : authors) { %>
   	 	<li><a href="<%= request.getContextPath() + "/servleti/author/" + u.getNick() %>"><%= HTMLSupport.escapeForHTMLBody(u.getFirstName() + " " + u.getLastName())%></a></li>  
    <% } %>  
    </ul>
	
	</body>
</html>