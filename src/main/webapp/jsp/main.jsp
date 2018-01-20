<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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
    <title>Ремонтное агенство::Вход</title>
    <link href="../css/style.css" rel="stylesheet" type="text/css">
    <link href="../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../css/login.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery.min.js"></script>
    <script src="../dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="header.jspx"/>
        <jsp:include page="mainmenu.jspx"/>
        <BR>
        <p class="title"><fmt:message key="app.welcome.title"/><BR></p>
        <p class="text"><fmt:message key="app.welcome.text"/>
        <p class="text">
        <p class="text">Проект находится в разработке, поэтому дизайн и функционал будет постоянно дорабатываться.</p>
        <p class="text">По всем вопросам обращайтесь по адресу <a href="mailto:mikhail.glushko@gmail.com">mikhail.glushko@gmail.com</a>
        </p>
        <p>&nbsp;</p>

        <jsp:include page="footer.jspx"/>
    </div>
</div>
</body>
</html>
