<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_13.model.Unos"%>
<%@page import="hr.fer.zemris.java.tecaj_13.web.HTMLSupport"%>
<%@page import="java.util.List"%>
<%
  Unos u = (Unos) request.getAttribute("unos");
%>
<html>
  <body>

  <h2>Unos <%= u.getId() %></h2>
	
	<b>Title:</b> <%= u.getTitle() %><br />
	<b>Message:</b> <%= u.getMessage() %><br />
	<b>User EMail:</b> <%= u.getUserEMail() %><br />
	<b>Created on:</b> <%= u.getCreatedOn() %><br />
	
  </body>
</html>
