<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.ListUsers"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listAdminCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listAdminAuthors&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListAuthors"/></a>
    <a href="controller?command=listAdminPublishings&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListPublishing"/></a>
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
        <input type="hidden" name="command" value="listAdminUsers"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.ListUsers"/></b>
            </div>
        </div>
    </div>

    <div class="w3-responsive">
        <table class="w3-table-all w3-card-4 w3-hoverable">
            <thead>
            <tr class="w3-light-grey">
                <th><fmt:message key="res.login"/></th>
                <th><fmt:message key="res.Description"/></th>
                <th><fmt:message key="res.State2"/></th>
                <th><fmt:message key="res.Librarian2"/></th>
                <th><fmt:message key="res.Cancel"/></th>
            </tr>
            </thead>
            <c:set var="k" value="0"/>
            <c:forEach var="item" items="${readersPage}">
                <c:set var="k" value="${k+1}"/>
                <tr>
                    <td>${item.username}</td>
                    <td>${item.description}</td>
                    <td>
                        <c:choose>
                            <c:when test="${item.active == 'true'}">
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="listAdminUsers"/>
                                    <button type="submit" name="stateOffId" value="${item.id}"
                                            class="w3-btn w3-green w3-round-large"><i class="material-icons">check_circle_outline</i></button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="listAdminUsers"/>
                                    <button type="submit" name="stateOnId" value="${item.id}"
                                            class="w3-btn w3-red w3-round-large"><i class="material-icons">block</i></button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.roleId == '2'}">
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="listAdminUsers"/>
                                    <button type="submit" name="libOffId" value="${item.id}"
                                            class="w3-btn w3-blue w3-round-large"><i class="material-icons">check_circle_outline</i></button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="listAdminUsers"/>
                                    <button type="submit" name="libOnId" value="${item.id}"
                                            class="w3-btn w3-gray w3-round-large"><i class="material-icons">block</i></button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="listAdminUsers"/>
                            <input type="hidden" name="show" value="all"/>
                            <button type="submit" name="cancelId" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large">
                                <fmt:message key="res.Cancel2"/></button>
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
                    <input type="hidden" name="command" value="listAdminUsers"/>
                    <input type="hidden" name="goPage" value="previous"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&lt;&lt;</button>
                </form>
            </div>
            <div class="w3-bar-item">
                <form action="controller" method="get">
                    <input type="hidden" name="command" value="listAdminUsers"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
