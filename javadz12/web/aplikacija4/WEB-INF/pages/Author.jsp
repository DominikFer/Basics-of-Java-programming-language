<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@page import="hr.fer.zemris.java.tecaj_14.web.HTMLSupport"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List" %>
<%
	
	boolean loggedIn = false;
	String loggedInUser = "Not logged in";
	if(session.getAttribute("current.user.id") != null) {
		loggedInUser = (String) session.getAttribute("current.user.fn") + " " + (String) session.getAttribute("current.user.ln");
		loggedIn = true;
	}
	
	List<BlogEntry> titles = (List<BlogEntry>) request.getAttribute("titles");
	
	String authorNick = (String) request.getAttribute("authorNick");
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>
	
	<h2><%= request.getAttribute("pageTitle") %> | Logiran kao: <%= loggedInUser %><%= loggedIn ? ", <a href=\"" + request.getContextPath() + "/servleti/logout\">Logout</a>" : "" %></h2>
	<h3>Titles by <%= authorNick %>:</h3>
	<ul>
    <% for(BlogEntry e : titles) { %>
   	 	<li><a href="<%= request.getContextPath() + "/servleti/author/" + authorNick + "/" + e.getId() %>"><%= HTMLSupport.escapeForHTMLBody(e.getTitle())%></a></li>  
    <% } %>  
    </ul>
    
	<%= loggedIn ? "<a href=\"" + request.getContextPath() + "/servleti/author/" + authorNick + "/new" + "\">Add new blog entry as " + authorNick + "</a>" : "" %>
	
	</body>
</html>