<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.SignIn"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <form action="controller" method="post">
        <input type="hidden" name="command" value="login"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding" align="center">
    <div class="w3-card-4" style="width: 30%">
        <div class="w3-container w3-center w3-green">
            <h3><fmt:message key="res.SignIn"/></h3>
        </div>
        <div class="w3-light-grey w3-padding w3-left-align">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="login"/>
                <label><fmt:message key="res.login"/>
                    <input type="text" name="login" class="w3-input w3-border w3-round-large" maxlength="45"><br/>
                </label>
                <label><fmt:message key="res.password"/>
                    <input type="password" name="password" class="w3-input w3-border w3-round-large"
                           maxlength="45"><br/>
                </label>
                <button type="submit" class="w3-btn w3-green w3-round-large w3-block"><fmt:message
                        key="res.loginIn"/></button>
            </form>

            <form action="controller" method="get">
                <input type="hidden" name="command" value="registration"/>
                <button type="submit" class="w3-btn w3-green w3-round-large w3-block"><fmt:message
                        key="res.RegistrationUser"/></button>
            </form>

        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>