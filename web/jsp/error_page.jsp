<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<h:head title="res.Error"></h:head>

<body class="w3-white">
<div class="w3-container w3-blue-grey w3-opacity w3-right-align">
    <h1>MyLibrary.com</h1>
</div>
<div class="w3-container w3-padding">

    <button type="submit" onclick="history.back();" class="w3-btn w3-green w3-round-large"><fmt:message
            key="res.Back"/></button>

    <h2>
        <fmt:message key="res.ErrorOccurred"/>
    </h2>

    <div class="w3-panel w3-yellow w3-border">
        <%-- this way we get the error information (error 404)--%>
        <c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
        <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>

        <%-- this way we get the exception --%>
        <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

        <c:if test="${not empty code}">
            <h3>Error code: ${code}</h3>
        </c:if>

        <c:if test="${not empty message}">
            <h3>Message: ${message}</h3>
        </c:if>

        <c:if test="${not empty errorMessage and empty exception and empty code}">
            <c:choose>
                <c:when test="${errorMessage == 'ErrorOrderExist'}">
                    <h3><fmt:message key="res.ErrorOrderExist"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorLoginPassEmpty'}">
                    <h3><fmt:message key="res.ErrorLoginPassEmpty"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorNotFindUser'}">
                    <h3><fmt:message key="res.ErrorNotFindUser"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorUserExists'}">
                    <h3><fmt:message key="res.ErrorUserExists"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorUserBlocked'}">
                    <h3><fmt:message key="res.ErrorUserBlocked"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorWrongData'}">
                    <h3><fmt:message key="res.ErrorWrongData"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorMoreThan45ch'}">
                    <h3><fmt:message key="res.ErrorMoreThan45ch"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorYearAndFine'}">
                    <h3><fmt:message key="res.ErrorYearAndFine"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorInvNumber'}">
                    <h3><fmt:message key="res.ErrorInvNumber"/></h3>
                </c:when>
                <c:when test="${errorMessage == 'ErrorSetAll'}">
                    <h3><fmt:message key="res.ErrorSetAll"/></h3>
                </c:when>
                <c:otherwise>
                    <h3>Error message: ${errorMessage}</h3>
                </c:otherwise>
            </c:choose>
        </c:if>

        <%-- this way we print exception stack trace --%>
        <c:if test="${not empty exception}">
            <hr/>
            <h3>Stack trace:</h3>
            <c:forEach var="stackTraceElement" items="${exception.stackTrace}">
                ${stackTraceElement}
            </c:forEach>
        </c:if>
    </div>

</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>