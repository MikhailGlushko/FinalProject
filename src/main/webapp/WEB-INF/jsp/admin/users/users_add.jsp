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
    <title>Ремонтное агенство::Добавление пользователя</title>
    <link href="../../../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../../../css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../../jsp/header.jspx"/>
        <jsp:include page="../../../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <div align="center">
        </div>
        <div class="login_div" align="center">
            <p class="title"><fmt:message key="app.welcome.pleaseregister"/></p>
            <form class="form-signin" name="register" method="post" action="<c:url value="/do"/>">
                <input type="hidden" name="command" value="users_action"/>
                <input type="hidden" name="page" value="${param.last_page}">
                <div class="row">
                    <div class="form-group">
                        <label for="user_login" style="float: left"><fmt:message key="app.welcome.login"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="user_login" type="text" name="user_login"
                               placeholder="<fmt:message key="app.welcome.login"/>"
                               value="${param.user_login}" required/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_password" style="float: left"><fmt:message key="app.welcome.password"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="user_password" type="password" name="user_password"
                               placeholder="<fmt:message key="app.welcome.password"/>"
                               value="${param.user_password}" required/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_password2" style="float: left"><fmt:message key="app.welcome.password2"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="user_password2" type="password" name="user_password2"
                               placeholder="<fmt:message key="app.welcome.password2"/>"
                               value="${param.user_password2}" required>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_name" style="float: left"><fmt:message key="app.welcome.name"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="user_name" type="text" name="user_name"
                               placeholder="<fmt:message key="app.welcome.name"/>"
                               value="${param.user_name}" required/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_email" style="float: left"><fmt:message key="app.welcome.email"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="user_email" type="email" name="user_email"
                               placeholder="<fmt:message key="app.welcome.email"/>"
                               value="${param.user_email}" required/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_phone" style="float: left"><fmt:message key="app.welcome.phone"/></label>
                        <input class="input-sm" style="float: right" id="user_phone" type="tel" name="user_phone"
                               placeholder="<fmt:message key="app.welcome.phone"/>"
                               value="${param.user_phone}"/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_role" style="float: left"><fmt:message key="app.welcome.role"/><em>*</em></label>
                        <select class="input-sm" style="float: right" id="user_role" name="user_role">
                            <option value="ADMIN">ADMIN</option>
                            <option value="MANAGER" selected>MANAGER</option>
                            <option value="MASTER">MASTER</option>
                            <option value="CUSTOMER" disabled>CUSTOMER</option>
                        </select>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label for="user_status" style="float: left"><fmt:message key="app.welcome.status"/><em>*</em></label>
                        <select class="input-sm" style="float: right" id="user_status" name="user_status">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="CLOSE">CLOSE</option>
                            <option value="BLOCKED" selected>BLOCKED</option>
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
                    <button class="btn btn-lg btn-primary btn-block" type="submit" name="action" value="add">
                        <fmt:message key='app.welcome.register'/>
                    </button>
                </div>
                <div class="ibput-row">
                    <button class="btn btn-sm btn-link" type="button" name="action" value="cancel"
                            onclick="window.location.href='/do?command=users'">
                        <fmt:message key='app.welcome.cancel'/>
                    </button>
                </div>
        </form>
        </div>
    </div>
    <jsp:include page="../../../../jsp/footer.jspx"/>
</div>
</body>
</html>
