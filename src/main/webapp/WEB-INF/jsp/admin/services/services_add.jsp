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
    <title>Ремонтное агенство::Добавление услуги</title>
    <link href="../../../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../../jsp/header.jspx"/>
        <jsp:include page="../../../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
            <div class="login_div" align="center">
                <form name="edit" method="post" action="/do">
                    <br/>
                    <input type="hidden" name="command" value="services_action"/>
                    <div class="row">
                        <div class="input-field">
                            <input id="service_name" type="text" name="service_name" value="" required/>
                            <label for="service_name"><fmt:message key="service.name"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="service_name_ru" type="text" name="service_name_ru" value="" required/>
                            <label for="service_name_ru"><fmt:message key="service.name.ru"/></label>
                        </div>
                    </div>

                    <div class="row">
                        <div class="input-field">
                            <input id="service_parent" type="number" name="service_parent" value="0" required/>
                            <label for="service_parent"><fmt:message key="service.parent"/></label>
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
                            <button name="action" class="mainmenubutton" type="submit" value="add">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                            <button name="action" class="mainmenubutton" type="button" value="cancel" onClick="window.location.href='/do?command=services'">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                    </div>
                </form>
            </div>
        <jsp:include page="../../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
