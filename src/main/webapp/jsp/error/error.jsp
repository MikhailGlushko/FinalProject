<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:if test="${empty cookie.locale.value or cookie.locale.value.equals('en')}">
    <fmt:setLocale value="en" scope="session"/>
</c:if>
<c:if test="${not empty cookie.locale.value and cookie.locale.value.equals('ru')}">
    <fmt:setLocale value="ru" scope="session"/>
</c:if>
<fmt:setBundle basename="messages"/>
<html>
<head><title>Error Page</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../css/login.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="main">
    <div class="content">
        <jsp:include page="../header.jspx"/>
        <jsp:include page="../mainmenu.jspx"/>
        <div class="error"></div>
        <div class="error"align="center">
            <fmt:message key="error.requestURI"/>${pageContext.errorData.requestURI} <fmt:message key="error.requestURIfail"/>
            <br/>
            <fmt:message key="error.servletName"/>${pageContext.errorData.servletName}
            <br/>
            <fmt:message key="error.statusCode"/>${pageContext.errorData.statusCode}
            <br/>
            <fmt:message key="error.exception"/>${pageContext.exception}
            <br/>
            <fmt:message key="error.message"/>${pageContext.exception.message}
        </div>
        <jsp:include page="../footer.jspx"/>
    </div>
</div>
</body>
</html>