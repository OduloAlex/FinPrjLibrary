<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>

<h:head title="res.MakeBook2"></h:head>

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
        <input type="hidden" name="command" value="makeAdminBook"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.MakeBook2"/></b>
            </div>
        </div>
    </div>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="makeAdminBook"/>
        <div class="w3-bar">
            <div class="w3-bar-item">
                <label><fmt:message key="res.Name"/>
                    <input name="name" class="w3-input w3-border w3-round-large" type="text" maxlength="45">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Author"/>
                    <select class="w3-select w3-border w3-round-large" name="authorId">
                        <option value="" disabled selected></option>
                        <c:forEach var="authorFor" items="${authors}">
                            <option value=${authorFor.id}>${authorFor.name}</option>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Publishing"/>
                    <select class="w3-select w3-border w3-round-large" name="publishingId">
                        <option value="" disabled selected></option>
                        <c:forEach var="publishingFor" items="${publishings}">
                            <option value=${publishingFor.id}>${publishingFor.name}</option>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Year"/>
                    <input name="year" class="w3-input w3-border w3-round-large" type="text" size="4" maxlength="4">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Description"/>
                    <input name="description" class="w3-input w3-border w3-round-large" type="text" maxlength="120">
                </label>
            </div>
            <div class="w3-bar-item">
                <label><fmt:message key="res.Fine"/>
                    <input name="fine" class="w3-input w3-border w3-round-large" type="text" size="4" maxlength="4">
                </label>
            </div>
        </div>

        <div class="w3-bar">
            <div class="w3-bar-item w3-right">
                <div class="w3-padding">
                    <button type="submit" name="save" value="ok" class="w3-btn w3-green w3-round-large"><fmt:message
                            key="res.Save"/></button>
                </div>
            </div>
        </div>
    </form>

</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
