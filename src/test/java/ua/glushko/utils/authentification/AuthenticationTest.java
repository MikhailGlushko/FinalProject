package ua.glushko.utils.authentification;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.utils.Authentication;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.services.utils.Authentication.PARAM_GRANTS;
import static ua.glushko.services.utils.Authentication.PARAM_ROLE;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import ua.glushko.transaction.H2DataSource;

public class AuthenticationTest {

    HttpSession session;
    HttpServletRequest request;
    @Before
    public void setUp() throws Exception {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        UsersService usersService = UsersService.getService();
        Map<User, List<Grant>> useDataAndGrantsSet = usersService.authenticateUser("admin", "P@ssw0rd");
        User user = useDataAndGrantsSet.keySet().iterator().next();
        List<Grant> grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_USERS);
    }

    @Test
    public void isUserLogIn() {
        boolean userLogIn = Authentication.isUserLogIn(session);
        assertFalse(userLogIn);
    }

    @Test
    public void checkAccess() throws ParameterException {
        int access = Authentication.checkAccess(request);
        assertTrue(access!=0);
    }
}