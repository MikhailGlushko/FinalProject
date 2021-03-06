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
            <div align="center">
                Внимание!
                Пароли должны удовлетворять следующим минимальным требованиям:<BR/>
                - Пароль не может содержать имя учетной записи пользователя или какую-либо его часть.<BR/>
                - Пароль должен состоять не менее чем из восьми символов.<BR/>
                - В пароле должны присутствовать символы из следующих четырех категорий:<BR/>
                прописные буквы английского алфавита от A до Z, строчные буквы английского алфавита от a до z;<BR/>
                десятичные цифры (от 0 до 9), неалфавитные символы (например, ~, !, @, #, @, %, ^, +, -, =, _, ., ,).<BR/>
            </div>
            <div class="login_div" align="center">
                <h2 class="form-signin-heading"><fmt:message key="app.welcome.pleaseregister"/></h2>
                <form class="form-signin" name="register" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="register"/>
                    <div class="input-row">
                        <div class="form-group">
                            <label class="sr-only" for="user_login"><fmt:message key="app.welcome.login"/><em>*</em></label>
                            <input class="form-control" id="user_login" type="text" name="user_login"
                                   placeholder="<fmt:message key="app.welcome.login"/>"
                                   value="<c:out value="${param.user_login}"/>" required/>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_password"><fmt:message key="app.welcome.password"/><em>*</em></label>
                            <input class="form-control" id="user_password" type="password" name="user_password"
                                   placeholder="<fmt:message key="app.welcome.password"/>"
                                   value="<c:out value="${param.user_password}"/>" required/>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_password2"><fmt:message key="app.welcome.password2"/><em>*</em></label>
                            <input class="form-control" id="user_password2" type="password" name="user_password2"
                                   placeholder="<fmt:message key="app.welcome.password2"/>"
                                   value="<c:out value="${param.user_password2}"/>" required>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_name"><fmt:message key="app.welcome.name"/><em>*</em></label>
                            <input class="form-control" id="user_name" type="text" name="user_name"
                                   placeholder="<fmt:message key="app.welcome.name"/>"
                                   value="<c:out value="${param.user_name}"/>" required/>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_email"><fmt:message key="app.welcome.email"/><em>*</em></label>
                            <input class="form-control" id="user_email" type="email" name="user_email"
                                   placeholder="<fmt:message key="app.welcome.email"/>"
                                   value="<c:out value="${param.user_email}"/>" required/>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <label class="sr-only" for="user_phone"><fmt:message key="app.welcome.phone"/></label>
                            <input class="form-control" id="user_phone" type="tel" name="user_phone"
                                   placeholder="<fmt:message key="app.welcome.phone"/>"
                                   value="<c:out value="${param.user_phone}"/>"/>
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
                        <button class="btn btn-lg btn-primary btn-block" type="submit" name="action" value="register">
                            <fmt:message key='app.welcome.register'/>
                        </button>
                    </div>
                    <div class="input-row">
                        <button class="btn btn-sm btn-link" type="button" style="float: left"
                                onClick="window.location.href='/jsp/recover.jsp'">
                            <fmt:message key='app.welcome.recover'/>
                        </button>
                        <button class="btn btn-sm btn-link" type="button" style="float: right"
                                onClick="window.location.href='/jsp/login.jsp'">
                            <fmt:message key='app.welcome.login'/>
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
