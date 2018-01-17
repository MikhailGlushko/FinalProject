package ua.glushko.commands.impl.admin.services;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.authentification.Authentification.PARAM_NAME_GRANTS;
import static ua.glushko.authentification.Authentification.PARAM_NAME_ROLE;
import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;
import static ua.glushko.commands.Command.PARAM_NAME_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_SERVICES;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class ServicesListCommandTest {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response=mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    List<Grant> grants;
    Map<User, List<Grant>> useDataAndGrantsSet;

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_NAME_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_NAME_COMMAND)).thenReturn(COMMAND_NAME_SERVICES);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void getUsersListForUserAdmin() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("admin", "admin");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserManager() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("manager", "manager");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("manager");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.MANAGER);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserMaster() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("master", "master");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("master");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.MASTER);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserCustomer() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("customer", "customer");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("customer");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = NullPointerException.class)
    public void getUsersListForGuest() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser(null, null);
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = PersistException.class)
    public void getUsersListForGuest2() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("test", "test");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}