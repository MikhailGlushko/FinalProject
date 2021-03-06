**Вариант 3.** 
Система Ремонтное Агенство. Пользователь может создать заявку на ремонт изделия. Менеджер может принять заявку указав цену, либо отклонить заявку, указав причину. Мастер может выполнить принятую Менеджером заявку. Пользователь может оставить Отзыв о выполненных работах.

**1.Исрользуемые технологии/фреймворки**
- Java 8;
- Servlet, JSP, JSTL;
- JDBC;
- mySQL, H2, Flyway;
- Log4J; 
- JUnit, Mockito;
- Apache Common (DBCP, Codec, Lang);
- Java Mail;
- HTML, CSS (Bootstrap);
- Git, Maven, Docker.

**2.Требования**
- JDK 1.8;
- Maven 3.5;
- Git 2.9;
- Tomcat 8;
- MySQL Server 5.6 с поддержкой utf-8.

**3.При разработка использовалось** 
- InteliJ Idea 2017 Idea UE;
- H2 Database v.1.4.196 (In-memory mode);
- MySQL Server v.5.6.36 (Docker Image, utf-8);
- Tomcat Server v.8.0.12 (Docker Image, jdk 1.8).

**4.Установка**
- Предполагается, что ПО, указанное в п.2 уже установлено, настроено и готово к работе;
- Клонировать репозитарий _`git clone https://github.com/MikhailGlushko/FinalProject`_;
- Запустить MySQL сервер, создать там схему repair_agency;
- Запустить Tomcat сервер;
- Настроить параметры подключения flyway-maven-plugin для развертывания базы данных _pom.xml: project/profiles/profile:prod/build/plugins/plugin:flyway-maven-plugin_ (пользователь с правами создания таблиц);
- Развернуть базу данных, выполнив из каталога проекта команду _`mvn -Pprod flyway:clean flyway:migrate`_;
- Настроить параметры подключения приложения к серверу MySQL _src/main/config/prod/META-INF/context.xml_ (пользователь с параметрами просмотра, добавления, удаления, изменения);
- Настроить параметры подключения maven к серверу Apache Tomcat _pom.xml: project/profiles/profile:prod/build/plugins/plugin:tomcat7-maven-plugin_;
- Выполнить из каталога проекта развертывание приложения командой _`mvn -Pprod tomcat7:redeploy`_.

**5.Запуск:**
перейти по полученной ссылке вида _`http://192.168.99.100:8080`_.

**6.Тестовые пользователи:**
- admin/P@ssw0rd - Администратор;
- manager/P@ssw0rd - Менеджер;
- master/P@ssw0rd - Мастер;
- customer/P@ssw0rd - Клиент.