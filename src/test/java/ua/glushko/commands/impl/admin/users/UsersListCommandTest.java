package ua.glushko.commands.impl.admin.users;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.GenericCommand;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.services.impl.UsersService;
import ua.glushko.servlets.Controller;
import ua.glushko.test.MyHttpServletRequest;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.authentification.Authentification.PARAM_NAME_GRANTS;
import static ua.glushko.authentification.Authentification.PARAM_NAME_ROLE;
import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_USERS;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class UsersListCommandTest {

    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response=mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp() throws Exception {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        UsersService usersService = UsersService.getService();
        Map<User, List<Grant>> useDataAndGrantsSet = usersService.authenticateUser("admin", "admin");
        List<Grant> grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);
        when(request.getParameter(PARAM_NAME_COMMAND)).thenReturn(COMMAND_NAME_USERS);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
   }

    @Test
    public void execute() throws ServletException, IOException {
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}