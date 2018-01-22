package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
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
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_REGISTER;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class RegisterCommandTest {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class, CALLS_REAL_METHODS);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp() {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_REGISTER);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void registerIncorrectData() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("admin");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request, response);
    }

    @Test (expected = DaoException.class)
    public void registerExist() throws ServletException, DaoException, TransactionException, ParameterException, DatabaseException {
            when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("P@ssw0rd");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn("Test User");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn("email@email.com");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn("+380(66)386-40-46");
            Controller controller = new Controller();
            controller.init();
            controller.processRequest(request, response);

            UsersService usersService = UsersService.getService();
            User test = usersService.getUserByLogin("testuser");
    }

    @Test
    public void register() throws ServletException, DaoException, TransactionException, ParameterException, DatabaseException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("testuser");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn("Test User");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn("+380(66)386-40-46");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request, response);

        UsersService usersService = UsersService.getService();
        User test = usersService.getUserByLogin("testuser");
    }
}