package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.servlets.MailServlet;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import ua.glushko.transaction.H2DataSource;

public class RecoveryCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class,CALLS_REAL_METHODS);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final Properties properties = new Properties();

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn("recovery");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        String PARAM_NAME_SMTP_HOST = "mail.smtp.host";
        String PARAM_NAME_SMTP_PORT = "mail.smtp.port";
        String PARAM_NAME_SMTP_USER = "mail.user.name";
        String PARAM_NAME_SMTP_PASS = "mail.user.password";
        properties.put(PARAM_NAME_SMTP_HOST, ConfigurationManager.getProperty(PARAM_NAME_SMTP_HOST));
        properties.put(PARAM_NAME_SMTP_PORT, ConfigurationManager.getProperty(PARAM_NAME_SMTP_PORT));
        properties.put(PARAM_NAME_SMTP_USER,ConfigurationManager.getProperty(PARAM_NAME_SMTP_USER));
        properties.put(PARAM_NAME_SMTP_PASS,ConfigurationManager.getProperty(PARAM_NAME_SMTP_PASS));

        when(request.getAttribute(RecoveryCommand.PARAM_MAIL_SETUP)).thenReturn(properties);
    }

    @Test
    public void resetPassword() {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        MailServlet controller = new MailServlet();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        //controller.service(request,response);
    }

    @Test
    public void resetPasswordNoExistUser() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("administrator");
        MailServlet controller = new MailServlet();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void resetPasswordNoExistUser2() throws ServletException, IOException {
        MailServlet controller = new MailServlet();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }
}