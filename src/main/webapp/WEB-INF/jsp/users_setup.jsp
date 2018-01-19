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
                <form name="edit" method="post" action="/do">
                    <br/>
                    <input type="hidden" name="command" value="setup_save"/>
                    <input type="hidden" name="user_id" value="${user_detail.id}">
                    <div class="row">
                        <div class="input-field">
                            <input id="user_login" type="text" name="user_login" value="${user_detail.login}" required/>
                            <label for="user_login"><fmt:message key="app.welcome.login"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input type="hidden" name="user_password_md5Hex" value="${user_detail.password}">
                            <input id="user_password" type="password" name="user_password" value="${param.password}" onclick="this.required=true"/>
                            <label for="user_password"><fmt:message key="app.welcome.password"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_password2" type="password" name="user_password2" value="${param.password2}">
                            <label for="user_password2"><fmt:message key="app.welcome.password2"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_name" type="text" name="user_name" value="${user_detail.name}" required/>
                            <label for="user_name"><fmt:message key="app.welcome.name"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_email" type="email" name="user_email" value="${user_detail.email}" required/>
                            <label for="user_email"><fmt:message key="app.welcome.email"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_phone" type="tel" name="user_phone" value="${user_detail.phone}"/>
                            <label for="user_phone"><fmt:message key="app.welcome.phone"/></label>
                        </div>
                    </div>
                ${errorMessage}
                    <br/>
                        ${wrongAction}
                    <br/>
                        ${nullPage}
                    <hr/>
                    <br/>
                    <div class="row">
                        <button class="mainmenubutton" type="submit" name="button" value="save">
                            <fmt:message key='app.welcome.save'/>
                        </button>
                        <button class="mainmenubutton" type="button" name="button" value="cancel" onClick="window.location.href='/'">
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
