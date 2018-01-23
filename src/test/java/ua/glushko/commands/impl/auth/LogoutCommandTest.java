package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_LOGOUT;
import ua.glushko.transaction.H2DataSource;

public class LogoutCommandTest {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class,CALLS_REAL_METHODS);
    HttpServletResponse response=mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_LOGOUT);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void logout() throws ServletException {
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
        verify(session).invalidate();
    }
}