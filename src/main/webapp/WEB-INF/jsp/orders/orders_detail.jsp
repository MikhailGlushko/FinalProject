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
            <div class="login_div" align="center" style="width: 550px; display: table-cell">
                <form name="edit" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="orders_action"/>
                    <input type="hidden" name="order_id" value="${orders_detail.id}"/>
                    <input type="hidden" name="page" value="${param.page}"/>
                    <c:if test="${role =='ADMIN'}">
                        <div class="input-row">
                            <div class="input-field">
                                <select id="order_repair_service" name="order_repair_service"
                                        value="${orders_detail.repairService}" required>
                                    <customtags:OptGoup list="${services_list}" value="${orders_detail.repairService}"/>
                                </select>
                                <label for="order_repair_service"><fmt:message key="order.repair.service"/><em>*</em></label>
                            </div>
                        </div>
                    </c:if>
                    <div class="input-row">
                        <div class="input-field">
                            <select id="order_status" name="order_status">
                                <option value="${orders_detail.status}">${orders_detail.status}</option>
                                <c:if test="${role =='ADMIN'}">
                                    <option value="NEW">NEW</option>
                                    <option value="CLOSE">CLOSE</option>
                                    <option value="COMPLETE">COMPLETE</option>
                                    <option value="SUSPEND">SUSPEND</option>
                                    <option value="INWORK">INWORK</option>
                                    <option value="REJECT">REJECT</option>
                                </c:if>
                            </select>
                            <label for="order_status"><fmt:message key="order.status"/></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                                <%--<input id="order_user_id" type="text" name="order_user_id" value="" required/>--%>
                            <select id="order_employee_id" name="order_employee_id">
                                    <%--<option value="${order_employee_id}" selected>${order_employee_name}</option>--%>
                                <option value="" disabled selected>${orders_detail.employeeName}</option>
                                <option value="${orders_detail.userId}">${order_user_name}</option>
                                <customtags:OptGoupStuff list="${employee_list}" value="${orders_detail.employeeId}"/>
                            </select>
                            <label for="order_employee_id"><fmt:message key="order.employee.id"/></label>
                        </div>
                    </div>
                    <fieldset>
                        <legend><fmt:message key="order.description"/></legend>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_description_short" type="text" name="order_description_short"
                                       value="${orders_detail.descriptionShort}" required/>
                                <label for="order_description_short"><fmt:message
                                        key="order.description.short"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                            <textarea id="order_description_detail" name="order_description_detail" required
                                      style="height: 100px">${orders_detail.descriptionDetail}</textarea>
                                <label for="order_description_detail"><fmt:message
                                        key="order.description.detail"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                    <%--<input id="order_appliance" type="text" name="order_appliance" value="${orders_detail.appliance}" required/>--%>
                                <textarea id="order_appliance" name="order_appliance" required
                                          style="height: 50px">${orders_detail.appliance}</textarea>
                                <label for="order_appliance"><fmt:message key="order.appliance"/><em>*</em></label>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <legend><fmt:message key="order.actionDate"/></legend>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_order_date" type="date" name="order_order_date"
                                       value="${orders_detail.orderDate}" disabled/>
                                <label for="order_order_date"><fmt:message key="order.order.actionDate"/></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_expected_date" type="date" name="order_expected_date"
                                       value="${orders_detail.expectedDate}"/>
                                <label for="order_expected_date"><fmt:message key="order.expected.actionDate"/></label>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <legend><fmt:message key="order.address"/></legend>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_city" type="text" name="order_city" value="${orders_detail.city}"
                                       required/>
                                <label for="order_city"><fmt:message key="order.city"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                    <%--<input id="order_street" type="text" name="order_street" value="${orders_detail.street}" required/>--%>
                                <textarea id="order_street" name="order_street" required
                                          style="height: 50px">${orders_detail.street}</textarea>
                                <label for="order_street"><fmt:message key="order.street"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                    <%--<input id="order_user_id" type="text" name="order_user_id" value="${orders_detail.userId}" required/>--%>
                                <select id="order_user_id" name="order_user_id">
                                    <option value="${orders_detail.userId}">${order_user_name}</option>
                                </select>
                                <label for="order_user_id"><fmt:message key="order.user.id"/></label>
                            </div>
                        </div>
                    </fieldset>
                    <div class="input-row">
                        <div class="input-field">
                            <input id="order_price" type="number" name="order_price"
                                   value="${orders_detail.price}"/>
                            <label for="order_price"><fmt:message key="order.price"/></label>
                        </div>
                    </div>
                    <div class="input-row">
                        <div class="input-field">
                            <textarea id="order_memo" type="text" name="order_memo"
                                      style="height: 100px">${orders_detail.memo}</textarea>
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
                    <c:if test="${role =='ADMIN'}">
                        <div class="input-row">
                            <button name="action" class="mainmenubutton" type="submit" value="save">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                            <button name="action" class="mainmenubutton" type="button" value="cancel"
                                    onClick="window.location.href='/do?command=orders&page=${page}'">
                                <fmt:message key='app.welcome.cancel'/>
                            </button>
                            <button name="action" class="mainmenubutton" type="submit" value="delete">
                                <fmt:message key='app.welcome.delete'/>
                            </button>
                        </div>
                    </c:if>
                </form>
            </div>


            <div class="login_div" align="center" style="width: 500px; display: table-cell">
                <form name="change" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="history_action"/>
                    <input type="hidden" name="order_id" value="${orders_detail.id}"/>
                    <input type="hidden" name="user_id" value="${userId}"/>
                    <fieldset>
                        <legend><fmt:message key="order.change.action"/></legend>
                        <div class="input-row">
                            <div class="input-field">
                                <select id="order_change_action" name="order_change_action" required>
                                    <option selected></option>
                                    <c:if test="${role !='CUSTOMER'}">
                                        <option value="CHANGE_EMPLOYEE"><fmt:message
                                                key="order.change.employee"/></option>
                                        <option value="CHANGE_STATUS"><fmt:message key="order.change.status"/></option>
                                        <option value="CHANGE_DATE"><fmt:message key="order.change.actionDate"/></option>
                                        <option value="CHANGE_PRICE"><fmt:message key="order.change.price"/></option>
                                        <option value="CHANGE_COMMENT"><fmt:message
                                                key="order.change.comment"/></option>
                                    </c:if>
                                    <c:if test="${role =='CUSTOMER'}">
                                        <option value="CHANGE_COMMENT" selected><fmt:message key="order.change.comment"/></option>
                                        <option value="GUESTBOOK_COMMENT"><fmt:message key="order.change.guestbook"/></option>
                                    </c:if>
                                </select>
                                <label for="order_change_action"><fmt:message key="order.change.action"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                            <textarea id="order_memo_change" type="text" name="order_memo_change"
                                      style="height: 50px" required></textarea>
                                <label for="order_memo_change"><fmt:message key="order.memo.change"/><em>*</em></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <button name="action" class="mainmenubutton" type="submit" value="change_employee">
                                <fmt:message key='app.welcome.apply'/>
                            </button>
                        </div>
                    </fieldset>
                    <c:if test="${role !='CUSTOMER'}">
                        <div class="input-row">
                            <div class="input-field">
                                    <%--<input id="order_user_id" type="text" name="order_user_id" value="" required/>--%>
                                <select id="order_employee_id_change" name="order_employee_id_change">
                                        <%--<option value="${order_employee_id}" selected>${order_employee_name}</option>--%>
                                    <option value="${orders_detail.userId}" disabled>${order_user_name}</option>
                                    <customtags:OptGoupStuff list="${employee_list}"
                                                             value="${orders_detail.employeeId}"/>
                                    <option value="" selected></option>
                                </select>
                                <label for="order_employee_id_change"><fmt:message key="order.employee.id"/></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                <select id="order_status_change" name="order_status_change">
                                    <option value="${orders_detail.status}" disabled="">${orders_detail.status}</option>
                                    <c:if test="${role !='MASTER'}">
                                        <option value="NEW">NEW</option>
                                        <option value="CLOSE">CLOSE</option>
                                        <option value="COMPLETE">COMPLETE</option>
                                        <option value="SUSPEND">SUSPEND</option>
                                        <option value="INWORK">INWORK</option>
                                        <option value="REJECT">REJECT</option>
                                    </c:if>
                                    <c:if test="${role =='MASTER'}">
                                        <option value="COMPLETE">COMPLETE</option>
                                        <option value="SUSPEND">SUSPEND</option>
                                        <option value="INWORK">INWORK</option>
                                        <option value="REJECT">REJECT</option>
                                    </c:if>
                                </select>
                                <label for="order_status_change"><fmt:message key="order.status"/></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_expected_date_change" type="actionDate" name="order_expected_date_change"
                                       value="${orders_detail.expectedDate}"/>
                                <label for="order_expected_date_change"><fmt:message key="order.expected.actionDate"/></label>
                            </div>
                        </div>
                        <div class="input-row">
                            <div class="input-field">
                                <input id="order_price_change" type="number" name="order_price_change"
                                       value="${orders_detail.price}"/>
                                <label for="order_price_change"><fmt:message key="order.price"/></label>
                            </div>
                        </div>
                    </c:if>
                </form>
                <hr/>
                <customtags:ShowOrdersHistoryList head="${history_list_head}" list="${history_list}"/>
            </div>
        </c:if>
        <jsp:include page="../../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
