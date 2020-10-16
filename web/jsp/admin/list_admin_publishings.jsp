<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>

<h:head title="res.ListPublishing"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listAdminCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listAdminUsers&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListUsers"/></a>
    <a href="controller?command=listAdminAuthors&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListAuthors"/></a>
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
        <input type="hidden" name="command" value="listAdminPublishings"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.ListPublishing"/></b>
            </div>
        </div>

        <form action="controller" method="post">
            <input type="hidden" name="command" value="listAdminPublishings"/>
            <div class="w3-bar-item w3-right">
                <button type="submit" class="w3-btn w3-green w3-round-large"><fmt:message
                        key="res.Make"/></button>
            </div>

            <div class="w3-bar-item w3-right">
                <input name="makePublishing" class="w3-input w3-border w3-round-large" type="text" maxlength="45"
                       placeholder=<fmt:message key="res.Publishing"/>>
            </div>
        </form>
    </div>

    <div class="w3-responsive">
        <table class="w3-table-all w3-card-4 w3-hoverable">
            <thead>
            <tr class="w3-light-grey">
                <th><fmt:message key="res.Publishing"/></th>
                <th><fmt:message key="res.Cancel"/></th>
            </tr>
            </thead>
            <c:set var="k" value="0"/>
            <c:forEach var="item" items="${publishingPage}">
                <c:set var="k" value="${k+1}"/>
                <tr>
                    <td>${item.name}</td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="listAdminPublishings"/>
                            <input type="hidden" name="show" value="all"/>
                            <button type="submit" name="cancelId" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large">
                                <i class="material-icons w3-large">delete_forever</i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="w3-center">
        <div class="w3-bar">
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listAdminPublishings"/>
                    <input type="hidden" name="goPage" value="previous"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&lt;&lt;</button>
                </form>
            </div>
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listAdminPublishings"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>

