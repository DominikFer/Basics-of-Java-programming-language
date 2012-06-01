<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <body style="background-color: <%= session.getAttribute("pickedBgCol") %>">
     <p><ul>
     <li><a href="colors.jsp">Background color chooser</a></li>
     <li><a href="squares?a=100&b=120">Squares</a></li>
     <li><a href="stories/funny.jsp">Funny story</a></li>
     <li><a href="report.jsp">OS Usage</a></li>
     <li><a href="powers?a=1&b=100&n=3">Excel file</a></li>
     <li><a href="appinfo.jsp">App Info</a></li>
     </ul></p>
   </body>
</html>