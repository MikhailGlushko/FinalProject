package ua.glushko.commands.impl.admin.orders;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.*;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.OrdersHistoryService;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.services.utils.Authentication.*;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.*;

/** Display information about the type of service with the ability to edit or delete */
public class OrderReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeOrderDetailToSession(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeOrderDetailToSession(HttpServletRequest request) throws TransactionException, ParameterException, DatabaseException {
        int pagesCount = 0;
        int rowsCount = 0;
        int pageNumber = 1;
        int id = 0;

        try {
            pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_PAGE);
            if (!(parameter == null || parameter.isEmpty()))
                pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
            id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
        } catch (NumberFormatException e){
            LOGGER.error(e);
        }
        int access = Authentication.checkAccess(request);

        // orders list
        OrdersService ordersService = OrdersService.getService();
        Order order = ordersService.getOrderById(id);
        List<String> titles = ordersService.getOrderTitles();

        // user data
        UsersService usersService = UsersService.getService();
        User client = usersService.getUserById(order.getUserId());
        Integer clientId = client.getId();
        String clientName = client.getName();

        // get all users list
        List<User> stuffs = usersService.getUsersByRole(UserRole.CUSTOMER, false);
        if ((access & R) == R || (access & r) == r) {
            // change all requests
            request.setAttribute(PARAM_ORDER_USER_ID,clientId);
            request.setAttribute(PARAM_ORDER_USER_NAME,clientName);
            request.setAttribute(OrdersCommandHelper.PARAM_ORDERS_LIST_TITLE, titles);
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
            request.setAttribute(OrdersCommandHelper.PARAM_EMPLOYEES_LIST,stuffs);
            // services list
            RepairServicesService repairServices = RepairServicesService.getService();
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            request.setAttribute(OrdersCommandHelper.PARAM_SERVICES_LIST, repairServiceList);
            // history list
            OrdersHistoryService ordersHistoryService = OrdersHistoryService.getService();
            List<OrderHistory> orderHistoryList = ordersHistoryService.getOrderHistoryList(pageNumber, pagesCount, rowsCount,id);
            List<String> orderHistoryTitles = ordersHistoryService.getOrderHistoryTitles();
            request.setAttribute(PARAM_ORDER_HISTORY_LIST,orderHistoryList);
            request.setAttribute(PARAM_ORDER_HISTORY_LIST_TITLE,orderHistoryTitles);
        }
    }
}