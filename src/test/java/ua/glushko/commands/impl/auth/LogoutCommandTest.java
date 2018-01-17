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

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;
import static ua.glushko.commands.Command.PARAM_NAME_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_LOGIN;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_LOGOUT;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class LogoutCommandTest {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class,CALLS_REAL_METHODS);
    HttpServletResponse response=mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_NAME_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_NAME_COMMAND)).thenReturn(COMMAND_NAME_LOGOUT);
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