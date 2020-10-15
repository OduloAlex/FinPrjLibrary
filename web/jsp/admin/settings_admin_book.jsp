<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>

<h:head title="res.SettingsBook"></h:head>

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
    <a href="controller?command=editAdminBook&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.EditBook"/></a>
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
        <input type="hidden" name="command" value="settingsAdminBook"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.SettingsBook"/></b>
            </div>
        </div>
    </div>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="settingsAdminBook"/>
        <div class="w3-bar">
            <div class="w3-bar-item">
                <label><fmt:message key="res.Name"/>
                    <input name="name" class="w3-input w3-border w3-round-large" type="text" value="${catalog.name}" maxlength="45">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Author"/>
                    <select class="w3-select w3-border w3-round-large" name="authorId">
                        <option value=${catalog.author.id}>${catalog.author.name}</option>
                        <c:forEach var="authorFor" items="${authors}">
                            <c:if test="${catalog.author.id != authorFor.id}">
                                <option value=${authorFor.id}>${authorFor.name}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Publishing"/>
                    <select class="w3-select w3-border w3-round-large" name="publishingId">
                        <option value=${catalog.publishing.id}>${catalog.publishing.name}</option>
                        <c:forEach var="publishingFor" items="${publishings}">
                            <c:if test="${catalog.publishing.id != publishingFor.id}">
                                <option value=${publishingFor.id}>${publishingFor.name}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Year"/>
                    <input name="year" class="w3-input w3-border w3-round-large" type="text" value="${catalog.year}"
                           size="4" maxlength="4">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Description"/>
                    <input name="description" class="w3-input w3-border w3-round-large" type="text"
                           value="${catalog.description}" maxlength="120">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Fine"/>
                    <input name="fine" class="w3-input w3-border w3-round-large" type="text" value="${catalog.fine}"
                           size="4" maxlength="4">
                </label>
            </div>
        </div>

        <div class="w3-bar">
            <div class="w3-bar-item w3-right">
                <div class="w3-padding">
                    <button type="submit" class="w3-btn w3-green w3-round-large"><fmt:message key="res.Save"/></button>
                </div>
            </div>
        </div>
    </form>

</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>