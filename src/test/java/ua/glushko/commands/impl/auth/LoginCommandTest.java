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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;
import static ua.glushko.commands.Command.PARAM_NAME_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_LOGIN;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class LoginCommandTest {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class,CALLS_REAL_METHODS);
    HttpServletResponse response=mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_NAME_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_NAME_COMMAND)).thenReturn(COMMAND_NAME_LOGIN);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void loginActiveUser() throws ServletException {
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD)).thenReturn("admin");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void loginBlockedUser() throws ServletException {
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("customer10");
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD)).thenReturn("customer10");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void loginFailure() throws ServletException {
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("");
        when(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD)).thenReturn("");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void loginNullPointer() throws ServletException {
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}