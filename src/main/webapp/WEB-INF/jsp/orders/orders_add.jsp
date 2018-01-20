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
        <BR>
        <p>&nbsp;</p>
        <div class="login_div" align="center">
            <form name="edit" method="post" action="/do">
                <input type="hidden" name="command" value="orders_action"/>
                <div class="input-row">
                    <div class="input-field">
                        <%--<input id="order_repair_service" type="text" name="order_repair_service" value="" required/>--%>
                        <select id="order_repair_service" name="order_repair_service" value="${param.order_repair_service}" required>
                            <customtags:OptGoup list="${services_list}"/>
                        </select>
                        <label for="order_repair_service"><fmt:message key="order.repair.service"/><em>*</em></label>
                    </div>
                </div>
                <fieldset>
                    <legend><fmt:message key="order.description"/></legend>
                    <div class="input-row">
                        <div class="input-field">
                            <input id="order_description_short" type="text" name="order_description_short" value="${param.order_description_short}"
                                   required/>
                            <label for="order_description_short"><fmt:message key="order.description.short"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                        <textarea id="order_description_detail" name="order_description_detail" required
                                  style="height: 100px">${param.order_description_detail}</textarea>
                            <label for="order_description_detail"><fmt:message key="order.description.detail"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <%--<input id="order_appliance" type="text" name="order_appliance" value="" required/>--%>
                            <textarea id="order_appliance" name="order_appliance" required
                                      style="height: 50px">${param.appliance}</textarea>
                            <label for="order_appliance"><fmt:message key="order.appliance"/><em>*</em></label>
                        </div>
                    </div>
                </fieldset>
                <fieldset>
                    <legend><fmt:message key="order.address"/></legend>
                    <div class="input-row">
                        <div class="input-field">
                            <input id="order_city" type="text" name="order_city" value="${param.order_city}" required/>
                            <label for="order_city"><fmt:message key="order.city"/><em>*</em></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <%--<input id="order_street" type="text" name="order_street" value="" required/>--%>
                            <textarea id="order_street" name="order_street" required style="height: 50px">${param.order_street}</textarea>
                            <label for="order_street"><fmt:message key="order.street"/><em>*</em></label>
                        </div>
                    </div>
                    <input type="hidden" name="order_user_id" value="${order_user_id}"/>
                </fieldset>
                <fieldset>
                    <legend><fmt:message key="order.actionDate"/></legend>
                    <div class="input-row">
                        <div class="input-field">
                            <input id="order_expected_date" type="date" name="order_expected_date" value="${param.order_expected_date}" required/>
                            <label for="order_expected_date"><fmt:message key="order.expected.actionDate"/><em>*</em></label>
                        </div>
                    </div>
                </fieldset>
                <div class="input-row">
                    <div class="input-field">
                        <textarea id="order_memo" type="text" name="order_memo" value="" style="height: 100px">${param.order_memo}</textarea>
                        <label for="order_memo"><fmt:message key="order.memo"/></label>
                    </div>
                </div>
                <div class="input-row">
                    <div class="input-field">
                        <select id="order_status" name="order_status" disabled>
                            <option value="NEW" selected>NEW</option>
                        </select>
                        <label for="order_status"><fmt:message key="order.status"/></label>
                    </div>
                </div>
                ${errorMessage}
                <br/>
                ${wrongAction}
                <br/>
                ${nullPage}
                <hr/>
                <br/>
                <div class="input-row">
                    <button name="action" class="mainmenubutton" type="submit" value="add">
                        <fmt:message key='app.welcome.save'/>
                    </button>
                    <button name="action" class="mainmenubutton" type="button" value="cancel"
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
