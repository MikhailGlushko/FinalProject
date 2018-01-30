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
    <title>Ремонтное агенство::Заказы</title>
    <link href="../../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../jsp/header.jspx"/>
        <jsp:include page="../../../jsp/mainmenu.jspx"/>
        <hr/>
        <!-- логика по выводу списка пользователей-->
        <c:if test="${not empty orders_list}">
            <br/>
            <br/>
            <fmt:message key="orders.title"/>:<br/><br/>
            <div class="input-row" style="width: 100%; float: left">
                <div class="btn btn-sm btn-success btm-sm-2" style="float: left;">
                    <fmt:message key="order.my.items"/> <span class="badge badge-success">${orders_count_my}</span>
                </div>
                <div class="btn btn-sm btn-info btn-sm-2" style="float: left;">
                    <fmt:message key="order.new.items"/><c:if test="${role=='MANAGER'}"><span class="badge badge-info" onClick="window.location.href='/do?command=orders_take'">${orders_count_new}</span></c:if>
                </div>
                <div class="btn btn-sm btn-warning btm-sm-2" style="float: left;">
                    <fmt:message key="order.my.progress"/> <span class="badge badge-warning">${orders_count_progress}</span>
                </div>
            </div>
            <br/>
            <br/>
            <customtags:ShowOrdersList head="${orders_list_head}" list="${orders_list}"/>
            <br/>
        </c:if>
        <hr/>
        <jsp:include page="../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
