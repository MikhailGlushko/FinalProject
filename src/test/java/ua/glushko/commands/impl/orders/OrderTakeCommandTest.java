package ua.glushko.commands.impl.orders;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.commands.utils.Authentication;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.UsersService;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.H2DataSource;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDER_TAKE;
import static ua.glushko.commands.utils.Authentication.PARAM_GRANTS;
import static ua.glushko.commands.utils.Authentication.PARAM_ROLE;

public class OrderTakeCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_ORDER_TAKE);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void take() throws ServletException, DatabaseException, TransactionException, IOException {
        UsersService usersService = UsersService.getService();
        Map<User, List<Grant>> useDataAndGrantsSet = usersService.authenticateUser("manager", "P@ssw0rd");
        List<Grant> grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("manager");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.MANAGER);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(session.getAttribute(Authentication.PARAM_ID)).thenReturn(2);
        OrdersService ordersService = OrdersService.getService();
        Order oldOrder = ordersService.getOrderById(1);
        Controller controller = new Controller();
        controller.init();
        when(request.getMethod()).thenReturn("POST");
        controller.service(request,response);
        Order updatedOrder = ordersService.getOrderById(1);
        assertNotEquals(oldOrder,updatedOrder);
    }
}