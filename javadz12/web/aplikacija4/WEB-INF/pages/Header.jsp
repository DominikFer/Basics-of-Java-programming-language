<%
	boolean loggedIn = false;
	String loggedInUser = "Not logged in";
	if(session.getAttribute("current.user.id") != null) {
		loggedInUser = (String) session.getAttribute("current.user.fn") + " " + (String) session.getAttribute("current.user.ln");
		loggedIn = true;
	}
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><%= request.getAttribute("pageTitle") %></title>
	</head>
	<body>
	
	<p><a href="<%= request.getContextPath() %>/servleti/main">Homepage</a></p>
	<h2><%= request.getAttribute("pageTitle") %> | Logiran kao: <%= loggedInUser %>, <%= loggedIn ? "<a href=\"" + request.getContextPath() + "/servleti/logout\">Logout</a>" : "<a href=\"" + request.getContextPath() + "/servleti/main\">Login</a>" %></h2>