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
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../header.jspx"/>
        <jsp:include page="../mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <c:if test="${not empty user_detail}">
            <div class="login_div" align="center">
                <form name="edit" method="post" action="/do">
                    <br/>
                    <input type="hidden" name="command" value="users_save"/>
                    <input type="hidden" name="user_id" value="${user_detail.id}">
                    <div class="row">
                        <div class="input-field">
                            <input id="user_name" type="text" name="user_name" value="${user_detail.name}" required/>
                            <label for="user_name"><fmt:message key="app.welcome.name"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_email" type="email" name="user_email" value="${user_detail.email}" required/>
                            <label for="user_email"><fmt:message key="app.welcome.email"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="user_phone" type="tel" name="user_phone" value="${user_detail.phone}"/>
                            <label for="user_phone"><fmt:message key="app.welcome.phone"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <select id="user_role" name="user_role">
                                <option value="${user_detail.role}">${user_detail.role}</option>
                                <option value="ADMIN">ADMIN</option>
                                <option value="MANAGER">MANAGER</option>
                                <option value="MASTER">MASTER</option>
                                <option value="CUSTOMER">CUSTOMER</option>
                            </select>
                            <label for="user_role"><fmt:message key="app.welcome.role"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <select id="user_status" name="user_status">
                                <option value="${user_detail.status}">${user_detail.status}</option>
                                <option value="ACTIVE">ACTIVE</option>
                                <option value="CLOSE">CLOSE</option>
                                <option value="BLOCKED">BLOCKED</option>
                            </select>
                            <label for="user_status"><fmt:message key="app.welcome.status"/></label>
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
                            <button class="mainmenubutton" type="button" name="button" value="cancel" onClick="window.location.href='/do?command=users'">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                            <button class="mainmenubutton" type="submit" name="button" value="delete">
                                <fmt:message key='app.welcome.delete'/>
                            </button>
                    </div>
                </form>
            </div>
        </c:if>
        <jsp:include page="../footer.jspx"/>
    </div>
</div>
</body>
</html>
