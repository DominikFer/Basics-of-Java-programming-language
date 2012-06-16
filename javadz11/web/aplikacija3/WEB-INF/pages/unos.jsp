<%@page import="hr.fer.zemris.java.tecaj_13.web.HTMLSupport"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_13.model.Unos"%>
<%@page import="hr.fer.zemris.java.tecaj_14.webforms.UnosForm"%>
<%@page import="hr.fer.zemris.java.tecaj_13.web.HTMLSupport"%>
<%
  	UnosForm unosForm = (UnosForm) request.getAttribute("model.object");

	String id = unosForm.getId() != null && !unosForm.getId().equals("0") ? "value=\"" + HTMLSupport.escapeForHTMLBody(unosForm.getId()) + "\"" : "";
	String title = unosForm.getTitle() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(unosForm.getTitle()) + "\"" : "";
	String message = unosForm.getMessage() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(unosForm.getMessage()) + "\"" : "";
	String EMail = unosForm.getUserEMail() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(unosForm.getUserEMail()) + "\"" : "";
	String createdOn = unosForm.getCreatedOn() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(unosForm.getCreatedOn()) + "\"" : "";
	
	String errors = "";
	if(unosForm.hasError()) {
		errors += "<ul>";
		if(unosForm.hasErrorFor("title")) errors += "<li>" + unosForm.getErrorFor("title") + "</li>";
		if(unosForm.hasErrorFor("message")) errors += "<li>" + unosForm.getErrorFor("message") + "</li>";
		if(unosForm.hasErrorFor("userEMail")) errors += "<li>" + unosForm.getErrorFor("userEMail") + "</li>";
		errors += "</ul>";
	}
%>
<html>
	<head>
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>

	<h2><%= request.getAttribute("pageTitle") %></h2>
	<form action="<%= request.getContextPath() %>/servleti/unos/save" method="get">
		<input type="hidden" id="id" name="id" <%= id %> />
		Title: <input type="text" id="title" name="title" <%= title %> /><br />
		Message: <input type="text" id="message" name="message" <%= message %> /><br />
		EMail: <input type="text" id="userEMail" name="userEMail" <%= EMail %> /><br />
		<input type="hidden" id="createdOn" name="createdOn" <%= createdOn %> />
		<input type="submit" value="Send" />
	</form>
	
	<%= errors %>
	
	</body>
</html>
