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
    <title>Ремонтное агенство::Редактирование pfrfpjd</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../jsp/header.jspx"/>
        <jsp:include page="../../jsp/mainmenu.jspx"/>
        <BR>
        <p>&nbsp;</p>
        <c:if test="${not empty orders_detail}">
            <div class="login_div" align="center">
                <form name="edit" method="post" action="/do">
                    <br/>
                    <input type="hidden" name="command" value="orders_action"/>
                    <input type="hidden" name="order_id" value="${orders_detail.id}">
                    <div class="row">
                        <div class="input-field">
                            <input id="order_description_short" type="text" name="order_description_short" value="${orders_detail.descriptionShort}" required/>
                            <label for="order_description_short"><fmt:message key="order.description.short"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <textarea id="order_description_detail" type="" name="order_description_detail"
                                      value="" required style="height: 100px">${orders_detail.descriptionDetail}</textarea>
                            <label for="order_description_detail"><fmt:message key="order.description.detail"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <%--<input id="order_repair_service" type="text" name="order_repair_service" value="${orders_detail.repairService}" required/>--%>
                                <select id="order_repair_service" name="order_repair_servive" value="${orders_detail.repairService}" required>
                                    <customtags:OptGoup list="${services_list}" value="${orders_detail.repairService}"/>
                                </select>
                                <label for="order_repair_service"><fmt:message key="order.repair.service"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_city" type="text" name="order_city" value="${orders_detail.city}" required/>
                            <label for="order_city"><fmt:message key="order.city"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_street" type="text" name="order_street" value="${orders_detail.street}" required/>
                            <label for="order_street"><fmt:message key="order.street"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_order_date" type="date" name="order_order_date" value="${orders_detail.orderDate}" required/>
                            <label for="order_order_date"><fmt:message key="order.order.date"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_time" type="text" name="order_time" value="${orders_detail.time}" required/>
                            <label for="order_time"><fmt:message key="order.time"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_appliance" type="text" name="order_appliance" value="${orders_detail.appliance}" required/>
                            <label for="order_appliance"><fmt:message key="order.appliance"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_paid_method" type="text" name="order_paid_method" value="${orders_detail.paidMethod}" required/>
                            <label for="order_paid_method"><fmt:message key="order.paid.method"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field">
                            <input id="order_user_id" type="text" name="order_user_id" value="${orders_detail.userId}" required/>
                            <label for="order_user_id"><fmt:message key="order.user.id"/></label>
                        </div>
                    </div><div class="row">
                    <div class="input-field">
                        <textarea id="order_memo" type="text" name="order_memo" value="" required style="height: 100px">${orders_detail.memo}</textarea>
                        <label for="order_memo"><fmt:message key="order.memo"/></label>
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
                            <button name="action" class="mainmenubutton" type="submit" value="save">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                            <button name="action" class="mainmenubutton" type="button" value="cancel" onClick="window.location.href='/do?command=orders&page=${page}'">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                            <button name="action" class="mainmenubutton" type="submit" value="delete">
                                <fmt:message key='app.welcome.delete'/>
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
