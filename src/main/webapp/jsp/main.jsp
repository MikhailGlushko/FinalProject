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
    <title>Ремонтное агенство::Вход</title>
    <link href="../css/style.css" rel="stylesheet" type="text/css">
    <link href="../dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="../css/login.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery.min.js"></script>
    <script src="../dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="main">
    <div class="content">
        <jsp:include page="header.jspx"/>
        <jsp:include page="mainmenu.jspx"/>
        <BR>
        <div class="row">
            <div class="col-lg-12">

            </div>
        </div>
        <BR>
        <div>
            <%--Центр--%>
            <div class="col-lg-9">
                <%--Приветствие--%>
                <div>
                    <div class="col-lg-3" id="slogan" style="text-align: center"><img src="../images/remont.png"
                                                                                      width="100%"></div>
                    <div class="col-lg-9">
                        <p class="text-justify"><fmt:message key="app.welcome.title"/><BR></p>
                        <p class="text-justify"><fmt:message key="app.welcome.text"/>
                        <p class="text-justify">Проект находится в разработке, поэтому дизайн и функционал будет
                            постоянно
                            дорабатываться.</p>
                        <p class="text-justify">По всем вопросам обращайтесь по адресу <a
                                href="mailto:mikhail.glushko@gmail.com">mikhail.glushko@gmail.com</a>
                        </p>
                    </div>
                </div>
                <%--Виды сервиса--%>
                <div class="col-lg-12">
                    <div class="panel panel-default" style="border: 1px solid #6cf; margin: 10px;">
                        <div class="panel-heading">
                            <h3 class="panel-title">Наши сервисы:</h3>
                        </div>
                        <div class="panel-body">
                            <div class="panel-body col-lg-6">

                                <div class="panel-heading" id="collapse-group">

                                    <customtags:Services list="${services_list}"/>

                                </div>
                            </div>
                            <div class="panel-body col-lg-6">
                                <img src="../images/texnika.jpg">
                            </div>
                        </div>
                    </div>
                </div>
                    <%----%>
                <div>
                    <%--Новости--%>
                    <div class="col-lg-6">
                        <div class="panel panel-default" style="border: 1px solid #6cf; margin: 10px;">
                            <div class="panel-heading">
                                <h4 class="panel-title">Последние новости</h4>
                            </div>
                            <div class="panel-body">
                                <customtags:News list="${news_list}"/>
                            </div>
                        </div>
                    </div>
                        <%--Отзывы--%>
                    <div class="col-lg-6">
                        <div class="panel panel-default" style="border: 1px solid #6cf; margin: 10px;">
                            <div class="panel-heading">
                                <h4 class="panel-title">Последние отзывы</h4>
                            </div>
                            <div class="panel-body">
                                <customtags:GuestBook list="${guestbook_list}" size="150"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--Правый край--%>
            <div class="col-lg-3">
                <div class="panel panel-primary" style="border: 1px solid #6cf; margin: 10px;">
                    <div class="panel-heading">
                        <h4 class="panel-title">Ремонт без хлопот</h4>
                    </div>
                    <div class="panel-body">
                        <div class="text-justify">Наш курьер сам доставит Вашу технику в мастерскую</div>
                        <img src="../images/courier.jpg" class="img-rounded" width="90%"/>
                    </div>
                    <div class="panel-footer"><h4 class="glyphicon glyphicon-envelope"></h4></div>
                </div>
                <BR>
                <div class="panel panel-primary" style="border: 1px solid #6cf; margin: 10px;">
                    <div class="panel-heading">
                        <h4 class="panel-title">Бесплатно</h4>
                    </div>
                    <div class="panel-body">
                        <div class="text-justify">Эксперт ответит на все Ваши вопросы и назовет точную стоимость
                            ремонта
                        </div>
                        <img src="../images/call.png" class="img-rounded" width="90%"/>
                        <div class="text-justify">Звоните в любое время суток, работает 24 часа без выходных!</div>
                    </div>
                    <div class="panel-footer">
                        <h4 class="glyphicon glyphicon-earphone"></h4>
                    </div>
                </div>
                <BR>
                <div class="panel panel-primary" style="border: 1px solid #6cf; margin: 10px;">
                    <div class="panel-heading">
                        <h4 class="panel-title">Нас можно найти</h4>
                    </div>
                    <div class="panel-body">
                        <img src="../images/map.jpg" class="img-thumbnail" width="100%"/>
                    </div>
                    <div class="panel-footer">
                        <h4 class="glyphicon glyphicon-pushpin"></h4>
                    </div>
                </div>
                <%--<c:forEach>--%>

                <%--</c:forEach>--%>
            </div>

        </div>
        <jsp:include page="footer.jspx"/>
    </div>
</div>
</body>
</html>
