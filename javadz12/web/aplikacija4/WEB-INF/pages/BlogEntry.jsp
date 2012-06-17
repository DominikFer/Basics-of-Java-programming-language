<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry,hr.fer.zemris.java.tecaj_14.model.BlogComment"%>
<%@page import="java.util.List"%>
<%
  	BlogEntry blogEntry = (BlogEntry) request.getAttribute("blog.entry");

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

    <p><%= blogEntry.getText() %></p>
    
    <%= (loggedIn && loggedInUserNick.equals(blogEntry.getCreator().getNick())) ? "<a href=\"" + request.getContextPath() + "/servleti/author/" + loggedInUserNick + "/edit?id=" + blogEntry.getId() + "\">Edit blog entry</a>" : "" %>
    
    <% if(!blogEntry.getComments().isEmpty()) { %>
	    <ul>
	    <% for(BlogComment c : blogEntry.getComments()) { %>
	    	<li><div style="font-weight: bold"><%= c.getUsersEMail() %> has posted on <%= c.getPostedOn() %> comment:</div><div style="padding-left: 10px;"><%= c.getMessage() %></div></li>
	    <% } %>  
	    </ul>
    <% } %>
    
    <% if(loggedIn) {%>
    	<form action="<%= request.getContextPath() %>/servleti/author/<%= blogEntry.getCreator().getNick() %>/<%= blogEntry.getId() %>" method="post">
			Comment text: <input type="text" id="comment" name="comment" /><br />
			<input type="submit" value="Send comment" />
		</form>
    <% } %>

  </body>
</html>