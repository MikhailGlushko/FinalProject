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
    <title>Ремонтное агенство::Редактирование пользователя</title>
    <link href="../../../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../../../css/login.css" rel="stylesheet" type="text/css">
    <link href="../../../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../../jsp/header.jspx"/>
        <jsp:include page="../../../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <c:if test="${not empty user_detail}">
            <div class="login_div" align="center">
                <form class="form-signin" name="edit" method="post" action="/do">
                    <input type="hidden" name="command" value="users_action"/>
                    <input type="hidden" name="user_id" value="${user_detail.id}"/>
                    <input type="hidden" name="page" value="${param.page}"/>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_name" style="float: left"><fmt:message key="app.welcome.name"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_name" type="text" name="user_name"
                                   placeholder="<fmt:message key="app.welcome.name"/>"
                                   value="${user_detail.name}" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_email" style="float: left"><fmt:message key="app.welcome.email"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_email" type="email" name="user_email"
                                   placeholder="<fmt:message key="app.welcome.email"/>"
                                   value="${user_detail.email}" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_phone" style="float: left"><fmt:message key="app.welcome.phone"/></label>
                            <input class="input-sm" style="float: right" id="user_phone" type="tel" name="user_phone"
                                   placeholder="<fmt:message key="app.welcome.phone"/>"
                                   value="${user_detail.phone}"/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_role" style="float: left"><fmt:message key="app.welcome.role"/><em>*</em></label>
                            <select class="input-sm" style="float: right" id="user_role" name="user_role">
                                <option value="${user_detail.role}">${user_detail.role}</option>
                                <option value="ADMIN">ADMIN</option>
                                <option value="MANAGER">MANAGER</option>
                                <option value="MASTER">MASTER</option>
                                <option value="CUSTOMER">CUSTOMER</option>
                            </select>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_status" style="float: left"><fmt:message key="app.welcome.status"/><em>*</em></label>
                            <select class="input-sm" style="float: right" id="user_status" name="user_status">
                                <option value="${user_detail.status}">${user_detail.status}</option>
                                <option value="ACTIVE">ACTIVE</option>
                                <option value="CLOSE">CLOSE</option>
                                <option value="BLOCKED">BLOCKED</option>
                            </select>
                        </div>
                    </div>
                    <br>
                    <c:if test="${not empty errorMessage}">
                        ${errorMessage}
                    </c:if>
                    <c:if test="${not empty wrongAction}">
                        <br/>                        ${wrongAction}
                    </c:if>
                    <c:if test="${not empty nullPage}">
                        <br/>                        ${nullPage}
                    </c:if>
                    <br>
                    <div class="input-row">
                            <button class="btn btn-lg btn-primary btn-block" type="submit" name="action" value="save">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                    </div>
                    <div class="input-row">
                            <button class="btn btn-sm btn-link" type="button" style="float: left" name="action" value="cancel" onClick="window.location.href='/do?command=users&page=${param.page}'">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                            <button class="btn btn-sm btn-link" type="submit" name="action" value="delete">
                                <fmt:message key='app.welcome.delete'/>
                            </button>
                    </div>
                </form>
            </div>
        </c:if>
        <jsp:include page="../../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
