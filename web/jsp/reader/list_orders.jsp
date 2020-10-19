<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.Orders"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listCards&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Cards"/></a>
    <a href="controller?command=logout" class="w3-bar-item w3-button w3-right"><fmt:message key="res.SignOut"/></a>
    <c:if test="${userRole.name == 'admin'}">
        <a href="controller?command=userSettings" class="w3-bar-item w3-button w3-right">${user.username}(<fmt:message
                key="res.Admin"/>)</a>
    </c:if>
    <c:if test="${userRole.name == 'librarian'}">
        <a href="controller?command=userSettings" class="w3-bar-item w3-button w3-right">${user.username}(<fmt:message
                key="res.Librarian"/>)</a>
    </c:if>
    <c:if test="${userRole.name == 'reader'}">
        <a href="controller?command=userSettings" class="w3-bar-item w3-button w3-right">${user.username}(<fmt:message
                key="res.Reader"/>)</a>
    </c:if>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="listOrders"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.Orders"/></b>
            </div>
        </div>
    </div>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="listOrders"/>
        <input type="hidden" name="show" value="all"/>
        <div class="w3-responsive">
            <table class="w3-table-all w3-card-4 w3-hoverable">
                <thead>
                <tr class="w3-light-grey">
                    <th><fmt:message key="res.Name"/></th>
                    <th><fmt:message key="res.Author"/></th>
                    <th><fmt:message key="res.Publishing"/></th>
                    <th><fmt:message key="res.Year"/></th>
                    <th><fmt:message key="res.Description"/></th>
                    <th><fmt:message key="res.Cancel"/></th>
                </tr>
                </thead>
                <c:set var="k" value="0"/>
                <c:forEach var="item" items="${ordersPage}">
                    <c:set var="k" value="${k+1}"/>
                    <tr>
                        <td>${item.catalogObj.name}</td>
                        <td>${item.catalogObj.author.name}</td>
                        <td>${item.catalogObj.publishing.name}</td>
                        <td>${item.catalogObj.year}</td>
                        <td>${item.catalogObj.description}</td>
                        <td><input type="checkbox" name="cancelId" value="${item.catalogObj.id}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <button type="submit" class="w3-bar-item w3-btn w3-green w3-round-large w3-margin w3-right"><fmt:message
                key="res.CancelOrder"/></button>
    </form>
    <div class="w3-center">
        <div class="w3-bar">
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listOrders"/>
                    <input type="hidden" name="goPage" value="previous"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&lt;&lt;</button>
                </form>
            </div>
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listOrders"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
