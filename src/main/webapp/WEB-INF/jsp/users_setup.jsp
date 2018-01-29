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
    <title>Ремонтное агенство::Настройка учетных данных</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../jsp/header.jspx"/>
        <jsp:include page="../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <c:if test="${not empty user_detail}">
            <div class="login_div" align="center">
                <form class="form-signin" name="edit" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="setup_save"/>
                    <input type="hidden" name="user_id" value="${user_detail.id}">
                    <input type="hidden" name="user_password_md5Hex" value="${user_detail.password}">
                    <div class="row">
                        <div class="form-group">
                            <label for="user_login" style="float: left"><fmt:message key="app.welcome.login"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_login" type="text" name="user_login"
                                   placeholder="<fmt:message key="app.welcome.login"/>"
                                   value="<c:out value="${user_detail.login}"/>" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_password_old" style="float: left"><fmt:message
                                    key="app.welcome.password.old"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_password_old" type="password" name="user_password_old"
                                   placeholder="<fmt:message key="app.welcome.password.old"/>"
                                   value="<c:out value="${param.user_password_old}"/>"/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_password" style="float: left"><fmt:message
                                    key="app.welcome.password"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_password" type="password" name="user_password"
                                   placeholder="<fmt:message key="app.welcome.password"/>"
                                   value="<c:out value="${param.user_password}"/>"/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_password2" style="float: left"><fmt:message
                                    key="app.welcome.password2"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_password2" type="password" name="user_password2"
                                   placeholder="<fmt:message key="app.welcome.password2"/>"
                                   value="<c:out value="${param.user_password2}"/>"/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_name" style="float: left"><fmt:message
                                    key="app.welcome.name"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_name" type="text" name="user_name"
                                   placeholder="<fmt:message key="app.welcome.name"/>"
                                   value="<c:out value="${user_detail.name}"/>" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_email" style="float: left"><fmt:message
                                    key="app.welcome.email"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="user_email" type="email" name="user_email"
                                   placeholder="<fmt:message key="app.welcome.email"/>"
                                   value="<c:out value="${user_detail.email}"/>" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group">
                            <label for="user_phone" style="float: left"><fmt:message key="app.welcome.phone"/></label>
                            <input class="input-sm" style="float: right" id="user_phone" type="tel" name="user_phone"
                                   placeholder="<fmt:message key="app.welcome.phone"/>"
                                   value="<c:out value="${user_detail.phone}"/>"/>
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
                    <div class="row">
                        <button class="btn btn-lg btn-primary btn-block" type="submit" name="button" value="save">
                            <fmt:message key='app.welcome.save'/>
                        </button>
                        <button class="btn btn-sm btn-link" type="button" name="button" value="cancel"
                                onClick="window.location.href='/'">
                            <fmt:message key='app.welcome.cancel'/>
                        </button>
                    </div>
                </form>
            </div>
        </c:if>
        <jsp:include page="../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
