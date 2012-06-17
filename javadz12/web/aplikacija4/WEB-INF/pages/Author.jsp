<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogUser"%>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry"%>
<%@page import="hr.fer.zemris.java.tecaj_14.web.HTMLSupport"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List" %>
<%
	
	boolean loggedIn = false;
	String loggedInUser = "Not logged in";
	String loggedInUserNick = null;
	if(session.getAttribute("current.user.id") != null) {
		loggedInUser = (String) session.getAttribute("current.user.fn") + " " + (String) session.getAttribute("current.user.ln");
		loggedInUserNick = (String) session.getAttribute("current.user.nick");
		loggedIn = true;
	}
	
	List<BlogEntry> titles = (List<BlogEntry>) request.getAttribute("titles");
	
	String authorNick = (String) request.getAttribute("authorNick");
	
%>
<html>
	<jsp:include page="Header.jsp" />
	
	<h3>Titles by <%= authorNick %>:</h3>
	<ul>
    <% for(BlogEntry e : titles) { %>
   	 	<li><a href="<%= request.getContextPath() + "/servleti/author/" + authorNick + "/" + e.getId() %>"><%= HTMLSupport.escapeForHTMLBody(e.getTitle())%></a></li>  
    <% } %>  
    </ul>
    
	<%= (loggedIn && loggedInUserNick.equals(authorNick)) ? "<a href=\"" + request.getContextPath() + "/servleti/author/" + authorNick + "/new" + "\">Add new blog entry</a>" : "" %>
	
	</body>
</html>