package ua.glushko.commands.impl.admin.users;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.authentification.Authentification.PARAM_GRANTS;
import static ua.glushko.authentification.Authentification.PARAM_ROLE;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class UsersListCommandTest {

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
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_USERS);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
   }

    @Test
    public void getUsersListForUserAdmin() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("admin", "admin");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserManager() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("manager", "manager");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("manager");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MANAGER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserMaster() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("master", "master");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("master");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MASTER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserCustomer() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("customer", "customer");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("customer");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = NullPointerException.class)
    public void getUsersListForGuest() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser(null, null);
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = PersistException.class)
    public void getUsersListForGuest2() throws ServletException, PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("test", "test");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}