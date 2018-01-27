Вариант 3. 
Система Ремонтное Агенство. Пользователь может создать заявку на ремонт изделия. Менеджер может принять заявку указав цену, либо отклонить заявку, указав причину. Мастер может выполнить принятую Менеджером заявку. Пользователь может оставить Отзыв о выполненных работах.

Для установки необходимо:
1. Установить JDK версии 8
2. Установить InteliJ IDEA UE последней версии.
3. Создать новый проект на основании данного репозитария. 
4. Установить и запустить Apache Tomcat версии 8 с поддержкой Java 8 или на целевой компьютер, или развернуть подходящий Docker Image.
5. Установить и запустить mySQL сервер, версии 5.7 с поддержкой UTF-8 на уровне сервера или на целевой компьютер, или развернкть подходящий Docker Image.
6. Настроить параметри подключения flyway-maven-plugin для развертывания базы данных в pom.xml: project/profiles/profile:prod/build/plugins/plugin:flyway-maven-plugin, предварительно создав схему repair_agency.
7. Развертуть базу данных, выполнив команду mvn -Pprod flyway:clean flyway:migrate.
8. Настроить параметры подключения приложения к серверу mySQL в файле src/main/config/prod/META-INF/context.xml.
9. Настроить параметри подключения maven к серверу Apache Tomcat в pom.xml: project/profiles/profile:prod/build/plugins/plugin:tomcat7-maven-plugin
10. Выполнить развертывание приложения командой mvn -Pprod tomcat7:redeploy
11. Перейти по полученной ссылке вида http://192.168.99.100:8080/ (http://127.0.0.1:8080/)
12. Тестовые пользователи:
- admin/P@ssw0rd - Администратор
- manager/P@ssw0rd - Менеджер
- master/P@ssw0rd - Мастер
- customer/P@ssw0rd - Клиент

Исрользуемые технологии/фреймворки
5.Технологии
- JSP API 2.1, JSTL 1.2, Servlet API 3.1.0;
- mySQL 6.0.6, H2 1.4.196, Flyway 5.0.6;
- Log4J 1.2.7, JUnit 4.12, Mockito 2.12;
- Apache Common DBCP 1.4, Apache Common Codec 1.11;
- Java Mail 1.5.0;
- Docker, Maven, Git;