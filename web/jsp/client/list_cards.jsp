<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Menu" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body class="w3-white">

<div class="w3-container w3-blue-grey w3-opacity w3-right-align">
    <h1>MyLibrary.com</h1>
</div>

<div class="w3-container w3-padding">
    <%--    <div class="w3-container w3-padding">--%>
    <%--        <%@ include file="/WEB-INF/jspf/header.jspf" %>--%>
    <%--    </div>--%>

    <div class="w3-container">
        <h2><b>Readers's cards</b></h2>
    </div>

    <form id="make_order" action="controller">
        <input type="hidden" name="command" value="makeOrder"/>
        <div class="w3-responsive">
            <table class="w3-table-all w3-card-4 w3-hoverable">
                <thead>
                <tr class="w3-light-grey">
                    <%--     <td><fmt:message key="list_menu_jsp.table.header.name"/></td>--%>
                    <th>№</th>
                    <th>Name</th>
                    <th>Year</th>
                    <th>Author</th>
                    <th>Publishing</th>
                    <th>Description</th>
                    <th>Order</th>
                </tr>
                </thead>
                <c:set var="k" value="0"/>
                <c:forEach var="item" items="${catalogItems}">
                    <c:set var="k" value="${k+1}"/>
                    <tr>
                        <td><c:out value="${k}"/></td>
                        <td>${item.name}</td>
                        <td>${item.year}</td>
                        <td>${item.author.name}</td>
                        <td>${item.publishing.name}</td>
                        <td>${item.description}</td>
                        <td><input type="checkbox" name="itemId" value="${item.id}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="w3-right">
            <button type="submit" class="w3-btn w3-green w3-round-large w3-margin">Order</button>
        </div>
    </form>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
