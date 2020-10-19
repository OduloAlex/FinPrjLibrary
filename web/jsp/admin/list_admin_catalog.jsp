<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.BookCatalog"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
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
        <input type="hidden" name="command" value="listAdminCatalog"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-bar">
        <div class="w3-bar-item">
            <div class="w3-padding w3-xlarge">
                <b><fmt:message key="res.BookCatalog"/></b>
            </div>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="makeAdminBook"/>
                <input type="hidden" name="show" value="all"/>
                <button type="submit" class="w3-btn w3-green w3-round-large"><fmt:message
                        key="res.MakeBook"/></button>
            </form>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listAdminCatalog"/>
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
                <input type="hidden" name="command" value="listAdminCatalog"/>
                <input name="findAuthor" class="w3-input w3-border w3-round-large" type="text"
                       onchange="this.form.submit()" placeholder=<fmt:message
                        key="res.Author"/>>
            </form>
        </div>

        <div class="w3-bar-item w3-right">
            <form action="controller" method="get">
                <input type="hidden" name="command" value="listAdminCatalog"/>
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
                <input type="hidden" name="command" value="listAdminCatalog"/>
                <input type="hidden" name="show" value="all"/>
                <button type="submit" class="w3-btn w3-light-grey w3-round-large"><fmt:message
                        key="res.ShowAll"/></button>
            </form>
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
                <th><fmt:message key="res.Description"/></th>
                <th><fmt:message key="res.Fine"/></th>
                <th><fmt:message key="res.Quantity"/></th>
                <th><fmt:message key="res.Edit"/></th>
                <th><fmt:message key="res.Cancel"/></th>
            </tr>
            </thead>
            <c:set var="k" value="0"/>
            <c:forEach var="item" items="${catalogPage}">
                <c:set var="k" value="${k+1}"/>
                <tr>
                    <td>${item.name}</td>
                    <td>${item.author.name}</td>
                    <td>${item.publishing.name}</td>
                    <td>${item.year}</td>
                    <td>${item.description}</td>
                    <td>${item.fine}</td>
                    <td>${item.quantity}</td>
                    <td>
                        <form action="controller" method="get">
                            <input type="hidden" name="command" value="editAdminBook"/>
                            <input type="hidden" name="show" value="all"/>
                            <button type="submit" name="editId" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large">
                                <i class="material-icons w3-large">settings</i></button>
                        </form>
                    </td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="listAdminCatalog"/>
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
                    <input type="hidden" name="command" value="listAdminCatalog"/>
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
                    <input type="hidden" name="command" value="listAdminCatalog"/>
                    <input type="hidden" name="goPage" value="next"/>
                    <button type="submit" class="w3-btn w3-light-grey w3-round-large w3-margin">&gt;&gt;</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>

