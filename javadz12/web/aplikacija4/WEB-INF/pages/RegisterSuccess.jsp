<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
	<head>
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>
	
	<h2><%= request.getAttribute("pageTitle") %></h2>
	<p>You have successfully registered on this website. Please proceed with <a href="<%= request.getContextPath() %>">login</a>.</p>
	
	</body>
</html>