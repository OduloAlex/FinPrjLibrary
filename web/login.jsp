<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Login"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body class="w3-white">
<div class="w3-container w3-blue-grey w3-opacity w3-right-align">
    <h1>MyLibrary.com</h1>
</div>

<div class="w3-container w3-padding" align="center">
    <div class="w3-card-4" style="width: 30%">
        <div class="w3-container w3-center w3-green">
            <h3>Sign in</h3>
        </div>
        <div class="w3-light-grey w3-padding w3-left-align">
            <form action="controller" method="get">
                <%--отправить command в атрибутах--%>
                <input type="hidden" name="command" value="login"/>

                <label><fmt:message key="login_jsp.label.login"/>
                    <input type="text" name="login" class="w3-input w3-border w3-round-large"><br/>
                </label>
                <label><fmt:message key="login_jsp.label.password"/>
                    <input type="password" name="password" class="w3-input w3-border w3-round-large"><br/>
                </label>
                <button type="submit" class="w3-btn w3-green w3-round-large w3-block"><fmt:message
                        key="login_jsp.button.login"/></button>
            </form>

            <form action="controller" method="get">
                <input type="hidden" name="command" value="toRegistration"/>
                <button type="submit" class="w3-btn w3-green w3-round-large w3-block">Registration</button>
            </form>

        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>