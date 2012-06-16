<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_13.model.Unos"%>
<%@page import="hr.fer.zemris.java.tecaj_13.web.HTMLSupport"%>
<%@page import="java.util.List"%>
<%
  List<Unos> unosi = (List<Unos>)request.getAttribute("unosi");
%>
<html>
  <body>

  <b>Pronađeni su sljedeći unosi:</b><br>

  <% if(unosi.isEmpty()) { %>
    Nema unosa.
  <% } else { %>
    <ul>
    <% for(Unos u : unosi) { %>
    <li>[ID=<%= u.getId() %>] <a href="<%= request.getContextPath() + "/servleti/details/" + u.getId() %>"><%= HTMLSupport.escapeForHTMLBody(u.getTitle())%></a> <a href="<%= request.getContextPath() + "/servleti/unos/edit/" + u.getId() %>">Edit</a></li>  
    <% } %>  
    </ul>
  <% } %>

	<a href="<%= request.getContextPath() + "/servleti/unos/new" %>">Add new record</a><br />
	<a href="<%= request.getContextPath() + "/servleti/pdfExport" %>">PDF export</a>

  </body>
</html>
