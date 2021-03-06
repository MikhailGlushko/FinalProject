package ua.glushko.commands.impl.admin.users;

import org.junit.Before;
import org.junit.Test;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.commands.utils.Authentication.PARAM_GRANTS;
import static ua.glushko.commands.utils.Authentication.PARAM_ROLE;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import ua.glushko.transaction.H2DataSource;

public class UsersListCommandTest {

    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private List<Grant> grants;
    private Map<User, List<Grant>> useDataAndGrantsSet;

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_USERS);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
   }

    @Test
    public void getUsersListForUserAdmin() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("admin", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void getUsersListForUserManager() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("manager", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("manager");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MANAGER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void getUsersListForUserMaster() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("master", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("master");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MASTER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void getUsersListForUserCustomer() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("customer", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("customer");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void getUsersListForGuest() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser(null, null);
        if(Objects.nonNull(useDataAndGrantsSet))
            grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }

    @Test
    public void getUsersListForGuest2() throws ServletException, TransactionException, SQLException, IOException {
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("test", "test");
        if(Objects.nonNull(useDataAndGrantsSet))
            grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.CUSTOMER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);

        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
    }
}