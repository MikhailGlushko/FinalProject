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
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../../jsp/header.jspx"/>
        <jsp:include page="../../../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <div align="center">
            Внимание!
            Пароли должны удовлетворять следующим минимальным требованиям:<BR/>
            - Пароль не может содержать имя учетной записи пользователя или какую-либо его часть.<BR/>
            - Пароль должен состоять не менее чем из восьми символов.<BR/>
            - В пароле должны присутствовать символы из следующих четырех категорий:<BR/>
            прописные буквы английского алфавита от A до Z;<BR/>
            строчные буквы английского алфавита от a до z;<BR/>
            десятичные цифры (от 0 до 9);<BR/>
            неалфавитные символы (например, ~, !, @, #, @, %, ^, +, -, =, _, ., ,).<BR/>
        </div>
        <div class="login_div" align="center">
            <p class="title"><fmt:message key="app.welcome.pleaseregister"/></p>
            <form name="register" method="post" action="/do">
                <input type="hidden" name="command" value="users_action"/>
                <div class="row">
                    <div class="input-field">
                        <input id="user_login" type="text" name="user_login" value="" required/>
                        <label for="user_login"><fmt:message key="app.welcome.login"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <input id="user_password" type="password" name="user_password" value="" required/>
                        <label for="user_password"><fmt:message key="app.welcome.password"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <input id="user_password2" type="password" name="user_password2" value="" required>
                        <label for="user_password2"><fmt:message key="app.welcome.password2"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <input id="user_name" type="text" name="user_name" value="" required/>
                        <label for="user_name"><fmt:message key="app.welcome.name"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <input id="user_email" type="email" name="user_email" value="" required/>
                        <label for="user_email"><fmt:message key="app.welcome.email"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <input id="user_phone" type="tel" name="user_phone"/>
                        <label for="user_phone"><fmt:message key="app.welcome.phone"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <select id="user_role" name="user_role">
                            <option value="ADMIN">ADMIN</option>
                            <option value="MANAGER" selected>MANAGER</option>
                            <option value="MASTER">MASTER</option>
                            <option value="CUSTOMER" disabled>CUSTOMER</option>
                        </select>
                        <label for="user_role"><fmt:message key="app.welcome.role"/></label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field">
                        <select id="user_status" name="user_status">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="CLOSE">CLOSE</option>
                            <option value="BLOCKED" selected>BLOCKED</option>
                        </select>
                        <label for="user_status"><fmt:message key="app.welcome.status"/></label>
                    </div>
                </div>
                    ${errorMessage}
                <br/> ${wrongAction}
                <br/> ${nullPage}
                <hr/>
                <br/>
                <div class="row">
                    <button class="mainmenubutton" type="submit" name="action" value="add">
                        <fmt:message key='app.welcome.register'/>
                    </button>
                    <button class="mainmenubutton" type="button" name="action" value="cancel"
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