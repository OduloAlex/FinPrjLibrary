<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 24.09.2020
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User list</title>
</head>
<body>
<div>
    <h1>MyLibrary.com</h1>
</div>
<div>
    <h2>User list:</h2>
</div>
<%
    List<String> names = (List<String>) request.getAttribute("userNames");

    if (names != null && !names.isEmpty()) {
        out.println("<ui>");
        for (String s : names) {
            out.println("<li>" + s + "</li>");
        }
        out.println("</ui>");
    } else out.println("<p>There are no users yet!</p>");
%>
<div>
    <button onclick="location.href='/'">back to main</button>
</div>
</body>
</html>
