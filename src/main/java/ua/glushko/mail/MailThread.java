package ua.glushko.mail;

import org.apache.log4j.Logger;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailThread extends Thread {

    private final Logger logger = Logger.getLogger(MailThread.class.getSimpleName());

    private MimeMessage message;
    private final String sendToEmail;
    private final String mailSubject;
    private final String mailText;
    private final Properties properties;
    public MailThread(String sendToEmail,
                      String mailSubject, String mailText, Properties properties) {
        this.sendToEmail = sendToEmail;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        this.properties = properties;
    }
    private void init() {
        // объект почтовой сессии
        String PARAM_NAME_SMTP_USER = "mail.user.name";
        Session mailSession = (new SessionCreator(properties)).createSession();
        mailSession.setDebug(true);
        // создание объекта почтового сообщения
        message = new MimeMessage(mailSession);
        try {
            // загрузка параметров в объект почтового сообщения
            message.setSubject(mailSubject);
            message.setContent(mailText, "text/html");
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            message.setFrom(properties.getProperty(PARAM_NAME_SMTP_USER));
        } catch (AddressException e) {
            logger.error("Некорректный адрес:" + sendToEmail + " " + e);
        } catch (MessagingException e) {
            logger.error("Ошибка формирования сообщения" + e);
        }
    }
    public void run() {
        init();
        try {
            // отправка почтового сообщения
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("Ошибка при отправлении сообщения" + e);
// in log file
        }
    }
}
