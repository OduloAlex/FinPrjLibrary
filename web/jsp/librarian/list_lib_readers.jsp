<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.ListReaders"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listLibCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
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
        <input type="hidden" name="command" value="listLibReaders"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.ListReaders"/></b>
            </div>
        </div>
    </div>

    <form action="controller" method="get">
        <input type="hidden" name="command" value="listLibReaders"/>
        <div class="w3-responsive">
            <table class="w3-table-all w3-card-4 w3-hoverable">
                <thead>
                <tr class="w3-light-grey">
                    <th><fmt:message key="res.login"/></th>
                    <th><fmt:message key="res.State"/></th>
                    <th><fmt:message key="res.Description"/></th>
                    <th><fmt:message key="res.Cards"/></th>
                    <th><fmt:message key="res.Orders"/></th>

                </tr>
                </thead>
                <c:set var="k" value="0"/>
                <c:forEach var="item" items="${readersPage}">
                    <c:set var="k" value="${k+1}"/>
                    <tr>
                        <td>${item.username}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.active == 'true'}">
                                    <fmt:message key="res.Active"/>
                                </c:when>
                                <c:when test="${item.active == 'false'}">
                                    <fmt:message key="res.Blocked"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${item.description}</td>
                        <td>
                            <button type="submit" name="itemIdCards" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large"><fmt:message key="res.Cards"/></button>
                        </td>
                        <td>
                            <button type="submit" name="itemIdOrders" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large"><fmt:message key="res.Orders"/></button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </form>
    <div class="w3-center">
        <div class="w3-bar">
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listLibReaders"/>
                    <input type="hidden" name="goPage" value="previous"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&lt;&lt;</button>
                </form>
            </div>
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listLibReaders"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
