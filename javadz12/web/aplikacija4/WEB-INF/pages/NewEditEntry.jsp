<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="hr.fer.zemris.java.tecaj_14.webforms.EntryForm"%>
<%@page import="hr.fer.zemris.java.tecaj_14.web.HTMLSupport"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List" %>
<%
	EntryForm entryForm = (EntryForm) request.getAttribute("model.object");

	String id = entryForm.getId() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(entryForm.getId()) + "\"" : null;
	String title = entryForm.getTitle() != null ? "value=\"" + HTMLSupport.escapeForHTMLBody(entryForm.getTitle()) + "\"" : "";
	String text = entryForm.getText() != null ? HTMLSupport.escapeForHTMLBody(entryForm.getText()) : "";
	
	String errors = "";
	if(entryForm.hasError()) {
		errors += "<ul>";
		for(Entry<String, String> error : entryForm.getErrors().entrySet()) {
			errors += "<li>" + error.getValue() + "</li>";
		}
		errors += "</ul>";
	}
	
	boolean loggedIn = false;
	String loggedInUser = "Not logged in";
	String loggedInUserNick = null;
	if(session.getAttribute("current.user.id") != null) {
		loggedInUser = (String) session.getAttribute("current.user.fn") + " " + (String) session.getAttribute("current.user.ln");
		loggedInUserNick = (String) session.getAttribute("current.user.nick");
		loggedIn = true;
	}
	
%>
<html>
	<jsp:include page="Header.jsp" />
	
	<form action="<%= request.getContextPath() %>/servleti/author/<%= loggedInUserNick %>" method="post">
		Title: <input type="text" id="title" name="title" <%= title %> /><br />
		Text: <textarea id="text" name="text" rows="15" cols="50"><%= text %></textarea><br />
		<input type="hidden" name="id" <%= id == null ? "": id %>/>
		<input type="submit" value="<%= id == null ? "Create" : "Save" %>" />
	</form>

	<%= errors %>
	
	</body>
</html>