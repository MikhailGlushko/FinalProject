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
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="header.jspx"/>
        <jsp:include page="mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <c:if test="${empty user_login}">
            <div class="login_div" align="center">
                <h2 class="form-signin-heading"><fmt:message key="app.welcome.pleaseLogin"/></h2>
                <form class="form-signin" name="loginForm" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="login"/>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_login"><fmt:message key="app.welcome.login"/></label>
                            <input class="form-control" id="user_login" type="text" name="user_login"
                                   placeholder="<fmt:message key="app.welcome.login"/>"
                                   value="<c:out value="${sessionScope.user_login}"/>" required/>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_password"><fmt:message key="app.welcome.password"/></label>
                            <input class="form-control" id="user_password" type="password" name="user_password"
                                   placeholder="<fmt:message key="app.welcome.password"/>"
                                   value="" required>
                        </div>
                    </div>
                    <c:if test="${not empty errorMessage}">
                        ${errorMessage}
                    </c:if>
                    <c:if test="${not empty wrongAction}">
                        <br/>                        ${wrongAction}
                    </c:if>
                    <c:if test="${not empty nullPage}">
                        <br/>                        ${nullPage}
                    </c:if>
                    <div class="input-row">
                        <button class="btn btn-lg btn-primary btn-block" type="submit" name="action">
                            <fmt:message key='app.welcome.enter'/>
                        </button>
                    </div>
                    <div class="input-row">
                        <button class="btn btn-sm btn-link" type="button" style="float: left"
                                onClick="window.location.href='/jsp/recover.jsp'">
                            <fmt:message key='app.welcome.recover'/>
                        </button>
                        <button class="btn btn-sm btn-link" type="button" style="float: right"
                                onClick="window.location.href='/jsp/register.jsp'">
                            <fmt:message key='app.welcome.register'/>
                        </button>
                    </div>
                </form>
            </div>
        </c:if>
        <jsp:include page="footer.jspx"/>
    </div>
</div>
</body>
</html>
