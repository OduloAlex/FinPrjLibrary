<%@ page import="app.domain.Card" %>
<%@ page import="java.util.Calendar" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Card" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listOrders&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Orders"/></a>
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
    <form action="controller" method="get">
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

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listCards"/>
                <select class="w3-select w3-border w3-round-large" name="sort" onchange="this.form.submit()">
                    <option value="" disabled selected><fmt:message key="res.Sort"/></option>
                    <option value="name"><fmt:message key="res.Name"/></option>
                    <option value="author"><fmt:message key="res.Author"/></option>
                    <option value="publishing"><fmt:message key="res.Publishing"/></option>
                    <option value="year"><fmt:message key="res.Year"/></option>
                </select>
            </form>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listCards"/>
                <input name="findAuthor" class="w3-input w3-border w3-round-large" type="text"
                       onchange="this.form.submit()" placeholder=<fmt:message
                        key="res.Author"/>>
            </form>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listCards"/>
                <input name="findName" class="w3-input w3-border w3-round-large" type="text"
                       onchange="this.form.submit()" placeholder=<fmt:message
                        key="res.Name"/>>
            </form>
        </div>

        <div class="w3-bar-item w3-right">
            <div class="w3-padding">
                <fmt:message key="res.FindBy"/>
            </div>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listCards"/>
                <input type="hidden" name="show" value="all"/>
                <button type="submit" class="w3-btn w3-light-grey w3-round-large"><fmt:message
                        key="res.ShowAll"/></button>
            </form>
        </div>
    </div>

    <%--    <form id="make_order" action="controller" method="post">--%>
    <%--        <input type="hidden" name="command" value="listCards"/>--%>
    <div class="w3-responsive">
        <table class="w3-table-all w3-card-4 w3-hoverable">
            <thead>
            <tr class="w3-light-grey">
                <%--     <td><fmt:message key="list_catalog_jsp.table.header.name"/></td>--%>
                <%--                    <th>№</th>--%>
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
                        <%--                        <td><c:out value="${k}"/></td>--%>
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
                        <%--                    <td><input type="checkbox" name="itemId" value="${k}"/></td>--%>
                </tr>
            </c:forEach>
        </table>
    </div>
    <%--        <button type="submit" class="w3-bar-item w3-btn w3-green w3-round-large w3-margin w3-right"><fmt:message--%>
    <%--                key="res.Order"/></button>--%>
    <%--    </form>--%>
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
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listCards"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>