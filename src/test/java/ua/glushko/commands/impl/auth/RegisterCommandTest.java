package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.DaoException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_REGISTER;
import ua.glushko.transaction.H2DataSource;

public class RegisterCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class, CALLS_REAL_METHODS);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_REGISTER);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void registerIncorrectData() throws ServletException, ParameterException, SQLException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("adm");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("admin");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);
        UsersService usersService = UsersService.getService();
        User test = usersService.getUserByLogin("adm");
        assertNull(test);
    }

    @Test
    public void registerExist() throws ServletException, IOException {
            when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("administrator");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("P@ssw0rd");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn("Test User");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn("email@email.com");
            when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn("+380(66)386-40-46");
            Controller controller = new Controller();
            controller.init();
            when(request.getMethod()).thenReturn("POST");
            controller.service(request, response);
            controller.service(request, response);
    }

    @Test
    public void register() throws ServletException, ParameterException, SQLException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("testuser");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn("Test User");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn("+380(66)386-40-46");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);

        UsersService usersService = UsersService.getService();
        User test = usersService.getUserByLogin("testuser");
        assertNotNull(test);
    }

    @Test
    public void register2() throws ServletException, ParameterException, SQLException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("testuser");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn("Test User");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn("+380(66)386-40-46");
        Controller controller = new Controller();
        controller.init();
        ConnectionPool.getConnectionPool().setDataSource(null);
        when(request.getMethod()).thenReturn("POST");
        controller.service(request, response);
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        UsersService usersService = UsersService.getService();
        User test = usersService.getUserByLogin("testuser");
        assertNull(test);
    }
}