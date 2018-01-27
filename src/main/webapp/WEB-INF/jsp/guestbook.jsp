<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>Ремонтное агенство::Заказы</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
    <link href="../../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../../css/login.css" rel="stylesheet" type="text/css">
    <script src="../../dist/js/jquery.min.js"></script>
    <script src="../../dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="../../jsp/header.jspx"/>
        <jsp:include page="../../jsp/mainmenu.jspx"/>
        <hr/>
        <br/>
        <fmt:message key="guestbook.title"/>:<br/><br/>
        <c:if test="${role=='CUSTOMER'}">
            <div class="login_div" align="center">
                <form name="new" method="post" action="<c:url value="/do"/>">
                    <input type="hidden" name="command" value="gestbook_new">
                    <legend>Новое сообщение</legend>
                    <div class="row">
                        <div class="form-group col-lg-12">
                            <label for="user_name" style="float: left">Автор сообщения<em>*</em></label>
                            <input class="form-control" id="user_name" style="float: right" value="" name="user_name"
                                   placeholder="Автор сообщения" required/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-lg-12">
                            <label for="subject" style="float: left">Тема сообщения<em>*</em></label>
                            <input class="form-control" id="subject" style="float: right" value="" name="subject"
                                   placeholder="Тема сообщения" required/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group  col-lg-12">
                            <label for="memo" style="float: left">Сообщение<em>*</em></label>
                            <BR>
                            <textarea class="form-control" id="memo" style="float: left; width: 100%; max-width: 100%"
                                      name="memo" rows="5" required></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <button class="btn btn-lg btn-primary btn-block" name="action" type="submit" value="add">
                                <fmt:message key='app.welcome.save'/>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </c:if>
        <c:if test="${not empty guestbook_list}">
            <div class="list" style="width: 100%">
                <div class="panel-group col-lg-offset-1 col-lg-10" id="collapse-group">
                    <c:forEach items="${guestbook_list}" var="item">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">
                                    <a data-toggle="collapse"
                                       data-parent="#collapse-group"
                                       href="#el${item.id}">${item.actionDate} ${item.userName} ${item.description}
                                    </a>
                                </h3>
                            </div>
                            <div id="el${item.id}" class="panel-collapse collapse">
                                <div class="panel-body">
                                        ${item.memo}
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <br/>
        </c:if>
        <hr/>
        <jsp:include page="../../jsp/footer.jspx"/>
    </div>
</div>
</body>
</html>
