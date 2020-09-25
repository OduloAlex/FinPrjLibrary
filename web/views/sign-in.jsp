<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign in</title>
</head>

<body>
<div>
    <h1>MyLibrary.com</h1>
</div>

<div>
    <%
        if (request.getAttribute("result") != null) {
            if((request.getAttribute("result").equals("ok"))&& (request.getAttribute("user_name") != null)) {
                    out.println("<p>Hello '" + request.getAttribute("user_name") + "'!</p>");
            }else if(request.getAttribute("result").equals("not_found")) {
                out.println("<p>User '" + request.getAttribute("user_name") + "' not found!</p>");
            } else {
                out.println("<p>Access denied</p>");
            }
        }
    %>
    <div>
        <div>
            <h2>Sign in:</h2>
        </div>

        <form method="post">
            <label>Name:
                <input type="text" name="name"><br />
            </label>
            <label>Password:
                <input type="password" name="pass"><br />
            </label>
            <button type="submit">sign in</button>
        </form>
    </div>
</div>

<div>
    <button onclick="location.href='/'">back to main</button>
</div>
</body>
</html>