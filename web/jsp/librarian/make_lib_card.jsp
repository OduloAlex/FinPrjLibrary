<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.MakeCard"></h:head>

<body class="w3-white">

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="w3-bar  w3-blue-grey ">
    <a href="controller?command=listLibCatalog&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Catalog"/></a>
    <a href="controller?command=listLibReaders&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.ListReaders"/></a>
    <a href="controller?command=listLibCards&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
            key="res.Cards"/></a>
    <a href="controller?command=listLibOrders&show=all" class="w3-bar-item w3-button w3-left"><fmt:message
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
    <form action="controller" method="post">
        <input type="hidden" name="command" value="makeLibCard"/>
        <c:forEach var="localeName" items="${locales}">
            <input type="submit" name="localeToSet" class="w3-bar-item w3-button w3-right" value=${localeName}>
        </c:forEach>
    </form>
</div>

<div class="w3-container w3-padding">
    <div class="w3-padding w3-xlarge">
        <b><fmt:message key="res.MakeCard"/> - ${reader.username}</b>
    </div>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="makeLibCard"/>
        <div class="w3-bar">
            <div class="w3-bar-item">
                <div class="w3-padding w3-large">
                    <b><fmt:message key="res.SelectCopyBook"/></b>
                </div>
            </div>
            <div class="w3-bar-item w3-right">
                    <input type="date" name="date" class="w3-input w3-border w3-round-large"/>
            </div>
            <div class="w3-bar-item w3-right">
                <div class="w3-padding">
                    <fmt:message key="res.ReturnDate"/>
                </div>
            </div>
            <div class="w3-bar-item w3-right">
                <select class="w3-select w3-border w3-round-large" name="state">
                    <option value="hand"><fmt:message key="res.OnHands"/></option>
                    <option value="room"><fmt:message key="res.InRoom"/></option>
                </select>
            </div>
            <div class="w3-bar-item w3-right">
                <div class="w3-padding">
                    <fmt:message key="res.State"/>
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
                    <th><fmt:message key="res.Make"/></th>
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
                            <button type="submit" name="makeId" value="${item.id}"
                                    class="w3-btn w3-green w3-round-large">
                                <fmt:message key="res.Make"/></button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </form>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
