<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="hr.fer.zemris.java.tecaj_14.webforms.RegisterForm"%>
<%@page import="hr.fer.zemris.java.tecaj_14.web.HTMLSupport"%>
<%@page import="java.util.Map.Entry;"%>
<%
  	RegisterForm registerForm = (RegisterForm) request.getAttribute("model.object");

	String firstName = registerForm.getFirstName() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(registerForm.getFirstName()) + "\"" : "";
	String lastName = registerForm.getLastName() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(registerForm.getLastName()) + "\"" : "";
	String nick = registerForm.getNick() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(registerForm.getNick()) + "\"" : "";
	String email = registerForm.getEmail() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(registerForm.getEmail()) + "\"" : "";
	String password = registerForm.getPassword() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(registerForm.getPassword()) + "\"" : "";
	
	String errors = "";
	if(registerForm.hasError()) {
		errors += "<ul>";
		for(Entry<String, String> error : registerForm.getErrors().entrySet()) {
			errors += "<li>" + error.getValue() + "</li>";
		}
		errors += "</ul>";
	}
%>
<html>
	<head>
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>

	<h2><%= request.getAttribute("pageTitle") %></h2>
	<form action="<%= request.getContextPath() %>/servleti/register" method="post">
		First name: <input type="text" id="firstName" name="firstName" <%= firstName %> /><br />
		Last name: <input type="text" id="lastName" name="lastName" <%= lastName %> /><br />
		Email: <input type="text" id="email" name="email" <%= email %> /><br />
		Nick: <input type="text" id="nick" name="nick" <%= nick %> /><br />
		Password: <input type="password" id="password" name="password" <%= password %> /><br />
		<input type="submit" value="Register" />
	</form>
	
	<%= errors %>
	
	</body>
</html>
