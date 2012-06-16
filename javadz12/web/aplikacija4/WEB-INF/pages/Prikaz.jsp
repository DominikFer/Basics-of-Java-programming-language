<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry,hr.fer.zemris.java.tecaj_14.model.BlogComment"%>
<%@page import="java.util.List"%>
<%
  BlogEntry blogEntry = (BlogEntry)request.getAttribute("blogEntry");
%>
<html>
  <body>

  <% if(blogEntry==null) { %>
    Nema unosa.
  <% } else { %>
    <h1><%= blogEntry.getTitle() %></h1>
    <p><%= blogEntry.getText() %></p>
    <% if(!blogEntry.getComments().isEmpty()) { %>
    <ul>
    <% for(BlogComment c : blogEntry.getComments()) { %>
    <li><div style="font-weight: bold">[Korisnik=<%= c.getUsersEMail() %>] <%= c.getPostedOn() %></div><div style="padding-left: 10px;"><%= c.getMessage() %></div></li>
    <% } %>  
    </ul>
    <% } %>
  <% } %>

  </body>
</html>