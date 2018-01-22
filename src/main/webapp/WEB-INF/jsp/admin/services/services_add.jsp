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
            <div class="login_div" align="center">
                <form name="edit" method="post" action="/do">
                    <input type="hidden" name="command" value="services_action"/>
                    <input type="hidden" name="last_page" value="${param.last_page}">
                    <div class="row">
                        <div class="form-group col-lg-12">
                            <label for="service_name" style="float: left"><fmt:message key="service.name"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="service_name" type="text" name="service_name"
                                   placeholder="<fmt:message key="service.name"/>"
                                   value="${param.service_name}" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group col-lg-12">
                            <label for="service_name_ru" style="float: left"><fmt:message key="service.name.ru"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="service_name_ru" type="text" name="service_name_ru"
                                   placeholder="<fmt:message key="service.name.ru"/>"
                                   value="${param.service_name_ru}" required/>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="form-group col-lg-12">
                            <label for="service_parent" style="float: left"><fmt:message key="service.parent"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="service_parent" type="number" name="service_parent"
                                   placeholder="<fmt:message key="service.parent"/>"
                                   value="${param.service_parent}" required/>
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
                            <button name="action" class="btn btn-lg btn-primary btn-block" type="submit" value="add">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                    </div>
                    <div class="input-row">
                            <button name="action" class="btn btn-sm btn-link" type="button" value="cancel" onClick="window.location.href='/do?command=services'">
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
