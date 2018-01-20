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
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../jsp/header.jspx"/>
        <jsp:include page="../../jsp/mainmenu.jspx"/>
        <hr/>
        <br/>
        <fmt:message key="guestbook.title"/>:<br/><br/>
        <div class="form">
            <form name="new" method="post" action="/do">
                <fieldset>
                    <input type="hidden" name="command" value="gestbook_new">
                    <legend>Новое сообщение</legend>
                    <div class="input-row">
                        <div class="input-field">
                            <input id="user_name" value="" name="user_name" required/>
                            <label for="user_name">Автор сообщения<em>*</em></label>
                        </div>
                        <div class="input-field">
                            <input id="subject" value="" name="subject" required/>
                            <label for="subject">Тема сообщения<em>*</em></label>
                        </div>
                        <div class="input-field">
                            <textarea id="memo" name="memo" required></textarea>
                            <label for="memo">Сообщение<em>*</em></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <button name="action" class="mainmenubutton" type="submit" value="add">
                            <fmt:message key='app.welcome.save'/>
                        </button>
                    </div>
                </fieldset>
            </form>
        </div>
        <c:if test="${not empty guestbook_list}">
            <div class="list" align="center" style="width: 100%">
                <c:forEach items="${guestbook_list}" var="item">
                    <fieldset>
                        <legend>${item.id}</legend>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="i1${item.id}" value="${item.userName}" disabled/>
                                <label for="i1${item.id}">Автор сообщения</label>
                            </div>
                            <div class="input-field">
                                <input id="i2${item.id}" value="${item.decription}" disabled/>
                                <label for="i2${item.id}">Тема сообщения</label>
                            </div>
                            <div class="input-field">
                                <input id="i3${item.id}" value="${item.actionDate}" disabled/>
                                <label for="i3${item.id}">Дата сообщения</label>
                            </div>
                            <div class="input-field">
                                <textarea id="i4${item.id}" disabled>${item.memo}</textarea>
                                <label for="i4${item.id}">Сообщение</label>
                            </div>
                        </div>
                    </fieldset>
                </c:forEach>
            </div>
            <br/>
        </c:if>
        <hr/>
        <jsp:include page="../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
