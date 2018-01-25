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
    <link href="../../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../../../css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../../jsp/header.jspx"/>
        <jsp:include page="../../../jsp/mainmenu.jspx"/>
        <c:if test="${not empty orders_detail}">
            <br/>
            <div class="login_div" align="center" style="width: 550px; display: table-cell">
                <form name="edit" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="orders_action"/>
                    <input type="hidden" name="order_id" value="${orders_detail.id}"/>
                    <input type="hidden" name="page" value="${param.page}"/>
                    <c:if test="${role =='ADMIN'}">
                        <div class="row" style="width: 100%">
                            <div class="form-group">
                                <label for="order_repair_service" style="float: left"><fmt:message
                                        key="order.repair.service"/><em>*</em></label>
                                <select class="input-sm" style="float: right" id="order_repair_service"
                                        name="order_repair_service"
                                        value="${orders_detail.repairService}" required>
                                    <customtags:OptGoup list="${services_list}" value="${orders_detail.repairService}"/>
                                </select>
                            </div>
                        </div>
                    </c:if>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label for="order_status" style="float: left"><fmt:message key="order.status"/></label>
                            <select class="input-sm" style="float: right" id="order_status" name="order_status">
                                <option value="${orders_detail.status}">${orders_detail.status}</option>
                                <c:if test="${role =='ADMIN'}">
                                    <option value="NEW">NEW</option>
                                    <option value="VERIFICATION">VERIFICATION</option>
                                    <option value="ESTIMATE">ESTIMATE</option>
                                    <option value="CONFIRMATION">CONFIRMATION</option>
                                    <option value="PROGRESS">PROGRESS</option>
                                    <option value="COMPLETE">COMPLETE</option>
                                    <option value="PAYMENT">PAYMENT</option>
                                    <option value="CLOSE">CLOSE</option>
                                    <option value="REJECT">REJECT</option>
                                    <option value="SUSPEND">SUSPEND</option>
                                </c:if>
                            </select>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_employee_id"><fmt:message
                                    key="order.employee.id"/></label>
                            <select class="input-sm" style="float: right" id="order_employee_id"
                                    name="order_employee_id">
                                <option value="" disabled selected>${orders_detail.employeeName}</option>
                                <option value="${orders_detail.userId}">${order_user_name}</option>
                                <customtags:OptGoupStuff list="${employee_list}" value="${orders_detail.employeeId}"/>
                            </select>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_description_short"><fmt:message
                                    key="order.description.short"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="order_description_short" type="text"
                                   name="order_description_short"
                                   placeholder="<fmt:message key="order.description.short"/>"
                                   value="${orders_detail.descriptionShort}" required/>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_description_detail"><fmt:message
                                    key="order.description.detail"/><em>*</em></label>
                            <textarea class="input-sm" id="order_description_detail" name="order_description_detail"
                                      required
                                      style="float: right; height: 100px">${orders_detail.descriptionDetail}</textarea>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_appliance"><fmt:message
                                    key="order.appliance"/><em>*</em></label>
                            <textarea class="input-sm" id="order_appliance" name="order_appliance" required
                                      style="float: right; height: 50px">${orders_detail.appliance}</textarea>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_order_date"><fmt:message
                                    key="order.order.actionDate"/></label>
                            <input class="input-sm" style="float: right" id="order_order_date" type="date"
                                   name="order_order_date"
                                   placeholder="<fmt:message key="order.order.actionDate"/>"
                                   value="${orders_detail.orderDate}" disabled/>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_expected_date"><fmt:message
                                    key="order.expected.actionDate"/></label>
                            <input class="input-sm" style="float: right" id="order_expected_date" type="date"
                                   name="order_expected_date"
                                   placeholder="<fmt:message key="order.expected.actionDate"/>"
                                   value="${orders_detail.expectedDate}"/>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_city"><fmt:message
                                    key="order.city"/><em>*</em></label>
                            <input class="input-sm" style="float: right" id="order_city" type="text" name="order_city"
                                   placeholder="<fmt:message key="order.city"/>"
                                   value="${orders_detail.city}"
                                   required/>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_street"><fmt:message
                                    key="order.street"/><em>*</em></label>
                            <textarea class="input-sm" id="order_street" name="order_street" required
                                      style="float: right; height: 50px">${orders_detail.street}</textarea>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_user_id"><fmt:message key="order.user.id"/></label>
                            <select class="input-sm" style="float: right" id="order_user_id" name="order_user_id">
                                <option value="${orders_detail.userId}">${order_user_name}</option>
                            </select>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_price"><fmt:message key="order.price"/></label>
                            <input class="input-sm" style="float: right" id="order_price" type="number"
                                   name="order_price"
                                   placeholder="<fmt:message key="order.price"/>"
                                   value="${orders_detail.price}"/>
                        </div>
                    </div>
                    <div class="row" style="width: 100%">
                        <div class="form-group">
                            <label style="float: left" for="order_memo"><fmt:message key="order.memo"/></label>
                            <textarea class="input-sm" id="order_memo" type="text" name="order_memo"
                                      style="float: right;height: 100px;">${orders_detail.memo}</textarea>
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
                    <c:if test="${role =='ADMIN' or role=='MANAGER' and orders_detail.status=='VERIFICATION'}">
                        <div class="input-row">
                            <button name="action" class="btn btn-sm btn-primary btn-block" type="submit" value="save">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                            <button name="action" class="btn btn-sm btn-link" type="button" value="cancel"
                                    onClick="window.location.href='/do?command=orders&page=${page}'"
                                    style="float: left">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                            <button name="action" class="btn btn-sm btn-link" type="submit" value="delete">
                                <fmt:message key='app.welcome.delete'/>
                            </button>
                        </div>
                    </c:if>
                </form>
            </div>

            <c:set var="statusId" value="${orders_detail.status.ordinal()}"/>
            <c:choose>
                <c:when test="${statusId==0}">
                    <c:set var="action_ok" value="VERIFICATION"/>
                </c:when>
                <c:when test="${statusId==1}">
                    <c:set var="action_ok" value="ESTIMATE"/>
                </c:when>
                <c:when test="${statusId==2}">
                    <c:set var="action_ok" value="CONFIRMATION"/>
                </c:when>
                <c:when test="${statusId==3}">
                    <c:set var="action_ok" value="PROGRESS"/>
                </c:when>
                <c:when test="${statusId==4}">
                    <c:set var="action_ok" value="COMPLETE"/>
                </c:when>
                <c:when test="${statusId==5}">
                    <c:set var="action_ok" value="PAYMENT"/>
                </c:when>
                <c:when test="${statusId==6}">
                    <c:set var="action_ok" value="CLOSE"/>
                </c:when>
                <c:when test="${statusId==9}">
                    <c:set var="action_ok" value="PROGRESS"/>
                </c:when>
            </c:choose>

            <div class="login_div" align="center" style="width: 500px; display: table-cell">
                <form name="change" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="orders_change_status"/>
                    <input type="hidden" name="order_id" value="${orders_detail.id}"/>
                    <input type="hidden" name="user_id" value="${id}"/>
                    <input type="hidden" name="order_status_change" value="${action_ok}">
                    <c:if test="${(orders_detail.employeeId==id and orders_detail.status!='CONFIRMATION') or
                                    (orders_detail.userId==id and orders_detail.status=='CONFIRMATION')}">
                        <c:choose>
                            <c:when test="${statusId==0}">
                                <div class="row" style="width: 100%">
                                    <div class="form-group">
                                        <label style="float: left" for="order_employee_id_change"><fmt:message
                                                key="order.employee.id"/></label>
                                        <select class="input-sm" style="float: right" id="order_employee_id_change"
                                                name="order_employee_id_change">
                                                <%--<option value="${order_employee_id}" selected>${order_employee_name}</option>--%>
                                            <option value="${orders_detail.userId}" disabled>${order_user_name}</option>
                                            <customtags:OptGoupStuff list="${employee_list}"
                                                                     value="${orders_detail.employeeId}"/>
                                            <option value="" selected></option>
                                        </select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${statusId==1}">
                                <div class="row" style="width: 100%">
                                    <div class="form-group">
                                        <label style="float: left" for="order_employee_id_change"><fmt:message
                                                key="order.employee.id"/></label>
                                        <select class="input-sm" style="float: right" id="order_employee_id_change"
                                                name="order_employee_id_change">
                                                <%--<option value="${order_employee_id}" selected>${order_employee_name}</option>--%>
                                            <option value="${orders_detail.userId}" disabled>${order_user_name}</option>
                                            <customtags:OptGoupStuff list="${employee_list}"
                                                                     value="${orders_detail.employeeId}"/>
                                            <option value="" selected></option>
                                        </select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                        </c:choose>
                        <c:if test="${statusId!=7 and statusId!=8}">
                            <div class="input-row">
                                <button name="action" class="btn btn-sm btn-success" type="submit" value="approve">
                                    OK and Sent to ${action_ok}
                                </button>
                            </div>
                        </c:if>
                    </c:if>
                    <br/>
                    <c:if test="${
                        (orders_detail.userId==id and (orders_detail.status=='CONFIRMATION' or
                                                       orders_detail.status=='NEW')) or
                        (orders_detail.employeeId==id and (orders_detail.status=='VERIFICATION' or
                                                           orders_detail.status=='ESTIMATE' or
                                                           orders_detail.status=='PROGRESS'))}">
                        <div class="row" style="width: 100%">
                            <div class="form-group">
                                <label for="reject_memo" style="float: left"><fmt:message
                                        key="order.rejection.reason"/></label>
                                <textarea class="input-sm" id="reject_memo" type="text" name="order_memo_change"
                                          style="float: right;height: 100px;">${reject_memo}</textarea>
                            </div>
                        </div>
                        <div class="input-row">
                            <button name="action" class="btn btn-sm btn-danger" type="submit" value="reject">
                                REJECT
                            </button>
                        </div>
                    </c:if>
                </form>
            </div>
        </c:if>
        <jsp:include page="../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
