package ua.glushko.authentification;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.authentification.Authentification.PARAM_NAME_GRANTS;
import static ua.glushko.authentification.Authentification.PARAM_NAME_ROLE;
import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_USERS;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class AuthentificationTest {

    HttpSession session;
    HttpServletRequest request;
    @Before
    public void setUp() throws Exception {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        UsersService usersService = UsersService.getService();
        Map<User, List<Grant>> useDataAndGrantsSet = usersService.authenticateUser("admin", "admin");
        User user = useDataAndGrantsSet.keySet().iterator().next();
        List<Grant> grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_NAME_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_NAME_GRANTS)).thenReturn(grants);
        when(request.getParameter(PARAM_NAME_COMMAND)).thenReturn(COMMAND_NAME_USERS);
    }

    @Test
    public void isUserLogIn() {
        boolean userLogIn = Authentification.isUserLogIn(session);
        System.out.println(userLogIn);
    }

    @Test
    public void checkAccess() {
        int access = Authentification.checkAccess(request);
        System.out.println(access);
    }
}