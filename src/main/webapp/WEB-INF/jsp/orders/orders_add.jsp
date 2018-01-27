<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="customtags" uri="customtags" %>
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
    <title>Ремонтное агенство::Добавление заказа</title>
    <link href="../../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../../../css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../jsp/header.jspx"/>
        <jsp:include page="../../../jsp/mainmenu.jspx"/>
        <div class="login_div" align="center">
            <form name="edit" method="post" action="<c:url value="/do"/>">
                <input type="hidden" name="command" value="orders_action"/>
                <div class="row" style="width: 100%">
                    <div class="form-group">
                        <label for="order_repair_service" style="float: left"><fmt:message
                                key="order.repair.service"/><em>*</em></label>
                        <select class="input-sm" style="float: right" id="order_repair_service"
                                name="order_repair_service"
                                value="${param.order_repair_service}" required>
                            <option value="" selected disabled><fmt:message key="order.repair.service"/></option>
                            <customtags:OptGoup list="${services_list}"/>
                        </select>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <label for="order_description_short" style="float: left"><fmt:message
                                key="order.description.short"/><em>*</em></label>
                        <input class="input-sm" style="float: right" id="order_description_short" type="text"
                               name="order_description_short"
                               placeholder="<fmt:message key="order.description.short"/>"
                               value="${param.order_description_short}" required/>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <label for="order_description_detail" style="float: left"><fmt:message
                                key="order.description.detail"/><em>*</em></label>
                        <textarea class="input-sm" id="order_description_detail" name="order_description_detail"
                                  required
                                  style="float: right; height: 100px">${param.order_description_detail}</textarea>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <label for="order_appliance" style="float: left"><fmt:message key="order.appliance"/><em>*</em></label>
                        <textarea class="input-sm" id="order_appliance" name="order_appliance" required
                                  style="float: right; height: 50px">${param.appliance}</textarea>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <input class="input-sm" style="float: right" id="order_city" type="text" name="order_city"
                               value="${param.order_city}" required/>
                        <label for="order_city" style="float: left"><fmt:message key="order.city"/><em>*</em></label>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <%--<input id="order_street" type="text" name="order_street" value="" required/>--%>
                        <textarea class="input-sm" id="order_street" name="order_street" required
                                  style="float: right; height: 50px">${param.order_street}</textarea>
                        <label for="order_street" style="float: left"><fmt:message
                                key="order.street"/><em>*</em></label>
                    </div>
                </div>
                <br/>
                <input type="hidden" name="order_user_id" value="${order_user_id}"/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <input class="input-sm" style="float: right" id="order_expected_date" type="date"
                               name="order_expected_date"
                               value="${param.order_expected_date}" required/>
                        <label for="order_expected_date" style="float: left"><fmt:message
                                key="order.expected.actionDate"/><em>*</em></label>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <textarea class="input-sm" id="order_memo" type="text" name="order_memo" value=""
                                  style="float: right;height: 100px">${param.order_memo}</textarea>
                        <label for="order_memo" style="float: left"><fmt:message key="order.memo"/></label>
                    </div>
                </div>
                <br/>
                <div class="row" style="width: 100%">
                    <div class="input-form">
                        <select class="input-sm" style="float: right" id="order_status" name="order_status" disabled>
                            <option value="NEW" selected>NEW</option>
                        </select>
                        <label for="order_status" style="float: left"><fmt:message key="order.status"/></label>
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
                    <button name="action" class="btn btn-sm btn-link" type="button" value="cancel"
                            onClick="window.location.href='/do?command=orders'">
                        <fmt:message key='app.welcome.cancel'/>
                    </button>
                </div>
            </form>
        </div>
        <jsp:include page="../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
