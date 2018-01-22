package ua.glushko.commands.impl.admin.orders;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
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
import static ua.glushko.services.utils.Authentication.PARAM_GRANTS;
import static ua.glushko.services.utils.Authentication.PARAM_ROLE;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class OrdersListCommandTest {
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
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_ORDERS);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void getUsersListForUserAdmin() throws ServletException, DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("admin", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(session.getAttribute(Authentication.PARAM_ID)).thenReturn(1);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserManager() throws ServletException, DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("manager", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("manager");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MANAGER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(session.getAttribute(Authentication.PARAM_ID)).thenReturn(2);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserMaster() throws ServletException, DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("master", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("master");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MASTER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(session.getAttribute(Authentication.PARAM_ID)).thenReturn(3);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void getUsersListForUserCustomer() throws ServletException, DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("customer", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("customer");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(session.getAttribute(Authentication.PARAM_ID)).thenReturn(4);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = DaoException.class)
    public void getUsersListForGuest() throws ServletException, DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser(null, null);
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test (expected = DaoException.class)
    public void getUsersListForGuest2() throws ServletException, DaoException, TransactionException, DatabaseException {
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