<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>

<h:head title="res.UserSettings"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=logout" class="w3-bar-item w3-button w3-right"><fmt:message key="res.SignOut"/></a>
    <div class="w3-bar-item w3-right">
        <c:if test="${userRole.name == 'admin'}">
            ${user.username}(<fmt:message key="res.Admin"/>)
        </c:if>
        <c:if test="${userRole.name == 'librarian'}">
            ${user.username}(<fmt:message key="res.Librarian"/>)
        </c:if>
        <c:if test="${userRole.name == 'reader'}">
            ${user.username}(<fmt:message key="res.Reader"/>)
        </c:if>
    </div>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="userSettings"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.UserSettings"/></b>
            </div>
        </div>
    </div>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="userSettings"/>
        <div class="w3-bar">
            <div class="w3-bar-item">
                <label><fmt:message key="res.newPassword"/>
                    <input name="password" class="w3-input w3-border w3-round-large" type="password"
                            maxlength="45">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Description"/>
                    <input name="description" class="w3-input w3-border w3-round-large" type="text"
                           value="${user.description}" size="40" maxlength="120">
                </label>
            </div>
            <div class="w3-bar-item w3-right">
                <div class="w3-padding">
                    <button type="submit" name="save" value="ok" class="w3-btn w3-green w3-round-large"><fmt:message
                            key="res.Save"/></button>
                </div>
            </div>
        </div>
    </form>
    <div class="w3-bar">
        <div class="w3-bar-item w3-right">
            <div class="w3-padding">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="userSettings"/>
                    <button type="submit" name="back" value="ok" class="w3-btn w3-green w3-round-large"><fmt:message
                            key="res.Back"/></button>
                </form>
            </div>
        </div>
    </div>

</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
