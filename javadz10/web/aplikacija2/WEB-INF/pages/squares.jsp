<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <body style="background-color: <%= session.getAttribute("pickedBgCol") %>">
     <p>
	     <table border="1">
	     	<tr><td>Number</td><td>Squared number</td></tr>
	     	<c:forEach var="counter" begin="${parameterA}" end="${parameterB}">
	     	<tr>
		     	<td>${counter}</td>
		     	<td><% 
		     		int counter = Integer.parseInt(pageContext.getAttribute("counter").toString());
		     		out.println(counter*counter);
		     	%></td>
		    </tr>
	     	</c:forEach>
	     </table>
     </p>
   </body>
</html>