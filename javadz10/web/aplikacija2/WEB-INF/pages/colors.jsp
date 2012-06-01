<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <body style="background-color: <%= session.getAttribute("pickedBgCol") %>">
     <p><ul>
     <li><a href="setcolor?color=white">WHITE</a></li>
     <li><a href="setcolor?color=red">RED</a></li>
     <li><a href="setcolor?color=green">GREEN</a></li>
     <li><a href="setcolor?color=cyan">CYAN</a></li>
     </ul></p>
   </body>
</html>