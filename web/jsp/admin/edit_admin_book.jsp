<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>

<h:head title="res.EditBook"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listAdminCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listAdminUsers&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListUsers"/></a>
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
        <input type="hidden" name="command" value="editAdminBook"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.EditBook"/></b>
            </div>
        </div>

        <form action="controller" method="post">
            <input type="hidden" name="command" value="editAdminBook"/>
            <div class="w3-bar-item w3-right">
                <div class="w3-margin-top">
                <button type="submit" class="w3-btn w3-green w3-round-large"><fmt:message
                        key="res.MakeCopy"/></button>
                </div>
            </div>
            <div class="w3-bar-item w3-right">
                <label><fmt:message key="res.SetInvNumber"/>
                    <input name="invNumber" class="w3-input w3-border w3-round-large" type="text" size="20"
                           maxlength="45">
                </label>
            </div>
        </form>
        <div class="w3-bar-item w3-right">
            <div class="w3-margin-bottom">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="settingsAdminBook"/>
                <button type="submit" class="w3-btn w3-green w3-round-large"><fmt:message
                        key="res.SettingsBook"/></button>
            </form>
            </div>
        </div>
    </div>

    <div class="w3-responsive">
        <table class="w3-table-all w3-card-4 w3-hoverable">
            <thead>
            <tr class="w3-light-grey">
                <th><fmt:message key="res.Name"/></th>
                <th><fmt:message key="res.Author"/></th>
                <th><fmt:message key="res.Publishing"/></th>
                <th><fmt:message key="res.Year"/></th>
                <th><fmt:message key="res.InvNumber"/></th>
                <th><fmt:message key="res.Fine"/></th>
                <th><fmt:message key="res.Cancel"/></th>
            </tr>
            </thead>
            <c:set var="k" value="0"/>
            <c:forEach var="item" items="${books}">
                <c:set var="k" value="${k+1}"/>
                <tr>
                    <td>${item.catalogObj.name}</td>
                    <td>${item.catalogObj.author.name}</td>
                    <td>${item.catalogObj.publishing.name}</td>
                    <td>${item.catalogObj.year}</td>
                    <td>${item.invNumber}</td>
                    <td>${item.catalogObj.fine}</td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="editAdminBook"/>
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
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>

