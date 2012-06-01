<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <body style="background-color: <%= session.getAttribute("pickedBgCol") %>">
     <p style="color: <%= request.getAttribute("storyColor") %>">
     	A group of computer science majors were listening to a lecture about Java programming at a university. After the lecture one of the men leaned over and grabbed a women's breast. 
<br /><br />Woman: "Hey! Thats private OK!?"
<br /><br />The man hesitated for a second looking confused.
<br /><br />Man: "But I thought we were in the same class?"
     </p>
   </body>
</html>