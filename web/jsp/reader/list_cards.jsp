<%@ page import="app.domain.Card" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.Cards"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listOrders&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Orders"/></a>
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
        <input type="hidden" name="command" value="listCards"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.Cards"/></b>
            </div>
        </div>
    </div>

    <c:if test="${not empty cardsPage}">
        <div class="w3-responsive">
            <table class="w3-table-all w3-card-4 w3-hoverable">
                <thead>
                <tr class="w3-light-grey">
                    <th><fmt:message key="res.Name"/></th>
                    <th><fmt:message key="res.Author"/></th>
                    <th><fmt:message key="res.Publishing"/></th>
                    <th><fmt:message key="res.Year"/></th>
                    <th><fmt:message key="res.InvNumber"/></th>
                    <th><fmt:message key="res.State"/></th>
                    <th><fmt:message key="res.Registration"/></th>
                    <th><fmt:message key="res.Return"/></th>
                    <th><fmt:message key="res.Fine"/></th>
                </tr>
                </thead>
                <c:set var="k" value="0"/>
                <c:forEach var="item" items="${cardsPage}">
                    <c:set var="k" value="${k+1}"/>
                    <tr>
                        <td>${item.book.catalogObj.name}</td>
                        <td>${item.book.catalogObj.author.name}</td>
                        <td>${item.book.catalogObj.publishing.name}</td>
                        <td>${item.book.catalogObj.year}</td>
                        <td>${item.book.invNumber}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.book.statusBookId == '1' }">
                                    <fmt:message key="res.InLibrary"/>
                                </c:when>
                                <c:when test="${item.book.statusBookId == '2'}">
                                    <fmt:message key="res.OnHands"/>
                                </c:when>
                                <c:when test="${item.book.statusBookId == '3'}">
                                    <fmt:message key="res.InRoom"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${Card.calendarToString(item.createTime)}</td>
                        <td>${Card.calendarToString(item.returnTime)}</td>
                        <td>
                            <c:if test="${Card.calendarIsAfter(item.returnTime) == true}">
                                ${item.book.catalogObj.fine}
                            </c:if>
                            <c:if test="${Card.calendarIsAfter(item.returnTime) == false}">
                                ---
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="w3-center">
            <div class="w3-bar">
                <div class="w3-bar-item">
                    <form action="controller" method="get">
                        <input type="hidden" name="command" value="listCards"/>
                        <input type="hidden" name="goPage" value="previous"/>
                        <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&lt;&lt;</button>
                    </form>
                </div>
                <div class="w3-bar-item">
                    <div class="w3-margin-top">
                            ${page}
                    </div>
                </div>
                <div class="w3-bar-item">
                    <form action="controller" method="get">
                        <input type="hidden" name="command" value="listCards"/>
                        <input type="hidden" name="goPage" value="next"/>
                        <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                    </form>
                </div>
            </div>
        </div>
    </c:if>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>