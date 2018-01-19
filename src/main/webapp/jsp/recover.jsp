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
                <p class="title"><fmt:message key="app.welcome.pleaseLogin"/></p>
                <form name="recoveryForm" method="post" action="/MailServlet">
                    <input type="hidden" name="command" value="recovery"/>
                    <div class="row">
                        <div class="input-field">
                            <label for="icon_prefix"><fmt:message key="app.welcome.login"/></label>
                            <input id="icon_prefix" type="text" name="user_login"
                                   value="${param.user_login}" required/>
                        </div>
                    </div>
                    <br/>
                    <br/>                        ${errorMessage}
                    <br/>                        ${wrongAction}
                    <br/>                        ${nullPage}
                    <hr/>
                    <br/>
                    <div class="row">
                        <div class="input-field">
                            <button class="mainmenubutton" type="submit" name="action">
                                <fmt:message key='app.welcome.enter'/>
                            </button>
                        </div>
                    </div>
                    <c:if test="${not empty errorMessage}">
                        <br/>
                        <br/>
                        <div class="row">
                            <div class="input-field">
                                <button class="mainmenubutton" type="button" onClick="window.location.href='/jsp/recover.jsp'">
                                    <fmt:message key='app.welcome.recover'/>
                                </button>
                            </div>
                        </div>
                    </c:if>
                </form>
            </div>
        </c:if>
        <jsp:include page="footer.jspx"/>
    </div>
</div>
</body>
</html>
