package ua.glushko.commands.impl.auth;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.services.impl.UsersService;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_LOGIN;
import ua.glushko.transaction.H2DataSource;

import java.io.IOException;
import java.sql.SQLException;

public class LoginCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class,CALLS_REAL_METHODS);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_LOGIN);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void loginActiveUser() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void loginActiveUser2() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        Controller controller = new Controller();
        controller.init();
        ConnectionPool.getConnectionPool().setDataSource(null);
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void loginBlockedUser() throws ServletException, SQLException, TransactionException, IOException {
        UsersService service = UsersService.getService();
        User user = service.getUserById(1);
        user.setStatus(UserStatus.BLOCKED);
        user.setPassword("P@ssw0rd");
        service.updateUser(user);
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("P@ssw0rd");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void loginFailure() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("admin");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void loginFailure2() throws ServletException, IOException {
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn("");
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void loginNullPointer() throws ServletException, IOException {
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }
}