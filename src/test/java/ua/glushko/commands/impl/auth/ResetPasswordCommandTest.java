package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_RESET_PASSWORD;
import ua.glushko.transaction.H2DataSource;

import java.io.IOException;

public class ResetPasswordCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class, CALLS_REAL_METHODS);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_RESET_PASSWORD);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession().getId()).thenReturn("DE22A9F939626B38ED9ABF7A0DA1A337");
    }

    @Test
    public void resetPassword() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("Comp@q00");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("Comp@q00");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_SECRET)).thenReturn("DE22A9F939626B38ED9ABF7A0DA1A337");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);
    }

    @Test
    public void resetPasswordIncorrectData() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("Comp@q00");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("Comp@q00");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);
    }

    @Test
    public void resetPasswordIncorrectLogin() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("administator");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("Comp@q00");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("Comp@q00");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_SECRET)).thenReturn("DE22A9F939626B38ED9ABF7A0DA1A337");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);
    }
}