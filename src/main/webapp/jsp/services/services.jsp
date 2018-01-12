<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="customtags" uri="customtags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<c:if test="${empty cookie.locale.value or cookie.locale.value.equals('en')}">
    <fmt:setLocale value="en" scope="session"/>
</c:if>
<c:if test="${not empty cookie.locale.value and cookie.locale.value.equals('ru')}">
    <fmt:setLocale value="ru" scope="session"/>
</c:if>

<fmt:setBundle basename="messages"/>
<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Ремонтное агенство::Пользователи</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../header.jspx"/>
        <jsp:include page="../mainmenu.jspx"/>
        <hr/>
        <!-- логика по выводу списка пользователей-->
        <c:if test="${not empty services_list}">
            <br/>
            <fmt:message key="services.title"/>:<br/><br/>
            <customtags:ShowServicesList head="${services_list_head}" list="${services_list}"/>
            <br/>
        </c:if>
        <hr/>
        <jsp:include page="../footer.jspx"/>
    </div>
</div>
</body>
</html>
