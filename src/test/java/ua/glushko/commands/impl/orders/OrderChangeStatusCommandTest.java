package ua.glushko.commands.impl.orders;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.*;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_ACTION_CRUD;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDER_CHANGE_STATUS;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;
import static ua.glushko.commands.utils.Authentication.PARAM_GRANTS;
import static ua.glushko.commands.utils.Authentication.PARAM_NAME_NAME;
import static ua.glushko.commands.utils.Authentication.PARAM_ROLE;

public class OrderChangeStatusCommandTest {

    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private List<Grant> grants;
    private Map<User, List<Grant>> useDataAndGrantsSet;
    private Order tmp;

    @Before
    public void setUp() throws DatabaseException, TransactionException {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(COMMAND_ORDER_CHANGE_STATUS);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        UsersService usersService = UsersService.getService();
        useDataAndGrantsSet = usersService.authenticateUser("admin", "P@ssw0rd");
        grants = useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        when(session.getAttribute(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn("admin");
        when(session.getAttribute(PARAM_ROLE)).thenReturn(UserRole.ADMIN);
        when(session.getAttribute(PARAM_GRANTS)).thenReturn(grants);
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)).thenReturn("1");
        OrdersService ordersService = OrdersService.getService();
        tmp = ordersService.getOrderById(1);
        assertNotNull(tmp);

        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_SHORT)).thenReturn(tmp.getDescriptionShort());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_DETAIL)).thenReturn(tmp.getDescriptionDetail());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_SERVICE)).thenReturn(String.valueOf(tmp.getRepairService()));
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_CITY)).thenReturn(tmp.getCity());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_STREET)).thenReturn(tmp.getStreet());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EXPECTED_DATE)).thenReturn(new Date(System.currentTimeMillis()).toString());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_APPL)).thenReturn(tmp.getAppliance());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_USER_ID)).thenReturn(String.valueOf(tmp.getUserId()));
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_MEMO)).thenReturn(tmp.getMemo());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EMPLOYEE_ID)).thenReturn(String.valueOf(tmp.getEmployeeId()));
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_STATUS)).thenReturn(tmp.getStatus().name());
        when(request.getParameter(OrdersCommandHelper.PARAM_ORDER_PRICE)).thenReturn(String.valueOf(tmp.getPrice()));

        when(request.getSession().getAttribute(PARAM_NAME_NAME)).thenReturn(useDataAndGrantsSet.keySet().iterator().next().getName());
    }

    @Test
    public void changeOrderStatus() throws ServletException, DaoException {
        when(request.getParameter(PARAM_ORDER_FORM_ACTION)).thenReturn(PARAM_ORDER_ACTION_APPROVE);
        when(request.getParameter(PARAM_ORDER_MEMO_CHANGE)).thenReturn("Прошу согласовать");
        when(request.getParameter(PARAM_ORDER_STATUS_CHANGE)).thenReturn(OrderStatus.VERIFICATION.name());
        when(request.getParameter(PARAM_ORDER_EMPLOYEE_ID_CHANGE)).thenReturn("3");
        OrdersService ordersService = OrdersService.getService();
        Order oldOrder = ordersService.getOrderById(1);
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
        Order order = ordersService.getOrderById(1);
        assertNotEquals(oldOrder,order);
    }

    @Test
    public void changeOrderStatus2() throws ServletException, DaoException {
        when(request.getParameter(PARAM_ORDER_FORM_ACTION)).thenReturn(PARAM_ORDER_ACTION_APPROVE);
        when(request.getParameter(PARAM_ORDER_MEMO_CHANGE)).thenReturn("Прошу согласовать");
        //when(request.getParameter(PARAM_ORDER_STATUS_CHANGE)).thenReturn(OrderStatus.VERIFICATION.name());
        //when(request.getParameter(PARAM_ORDER_EMPLOYEE_ID_CHANGE)).thenReturn("3");
        OrdersService ordersService = OrdersService.getService();
        Order oldOrder = ordersService.getOrderById(1);
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
        Order order = ordersService.getOrderById(1);
        assertEquals(oldOrder,order);
    }

    @Test
    public void reject() throws ServletException {
        when(request.getParameter(PARAM_ORDER_FORM_ACTION)).thenReturn(PARAM_ORDER_ACTION_REJECT);
        when(request.getParameter(PARAM_ORDER_MEMO_CHANGE)).thenReturn("Нет запчастей");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void фввСщььуте() throws ServletException {
        when(request.getParameter(PARAM_ORDER_FORM_ACTION)).thenReturn(PARAM_ORDER_ACTION_COMMENT);
        when(request.getParameter(PARAM_ORDER_MEMO_CHANGE)).thenReturn("Нет запчастей");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}