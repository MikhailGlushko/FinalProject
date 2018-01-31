package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.*;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.utils.Authentication.*;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;

/**
 * Admin Order Management Command, which prepare detail data to show in form
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrderReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        OrdersService ordersService = OrdersService.getService();
        UsersService usersService = UsersService.getService();
        RepairServicesService repairServices = RepairServicesService.getService();
        try {
            if(isUserHasRightToRead(request)){
                prepareDataToShow(request, ordersService, usersService, repairServices);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void prepareDataToShow(HttpServletRequest request, OrdersService ordersService, UsersService usersService, RepairServicesService repairServices) throws ParameterException, SQLException {
        Order order = prepareOrderData(request, ordersService);
        prepareClientData(request, usersService, order);
        prepareStuffdata(request, usersService);
        prepareServiceData(request, repairServices);
    }

    private void prepareServiceData(HttpServletRequest request, RepairServicesService repairServices) throws SQLException {
        List<RepairService> repairServiceList = repairServices.getRepairServiceList();
        request.setAttribute(PARAM_SERVICES_LIST, repairServiceList);
        request.setAttribute(PARAM_ORDERS_STAT, OrderStatus.values());
    }

    private Order prepareOrderData(HttpServletRequest request, OrdersService ordersService) throws ParameterException, SQLException {
        int orderId = getOrderId(request);
        Order order = ordersService.getOrderById(orderId);
        List<String> titles = ordersService.getOrderTitles();
        storeOrderData(request, order, titles);
        return order;
    }

    private void prepareStuffdata(HttpServletRequest request, UsersService usersService) throws SQLException {
        List<User> stuffs = usersService.getUsersByRole(UserRole.CUSTOMER, false);
        storeStuffData(request, stuffs);
    }

    private void prepareClientData(HttpServletRequest request, UsersService usersService, Order order) throws SQLException {
        User client = usersService.getUserById(order.getUserId());
        storeUserData(request, client);
    }

    private void storeStuffData(HttpServletRequest request, List<User> stuffs) {
        request.setAttribute(OrdersCommandHelper.PARAM_EMPLOYEES_LIST, stuffs);
    }

    private void storeOrderData(HttpServletRequest request, Order order, List<String> titles) {
        request.setAttribute(OrdersCommandHelper.PARAM_ORDERS_LIST_TITLE, titles);
        request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
    }

    private void storeUserData(HttpServletRequest request, User client) {
        Integer clientId = client.getId();
        String clientName = client.getName();
        request.setAttribute(PARAM_ORDER_USER_ID, clientId);
        request.setAttribute(PARAM_ORDER_USER_NAME, clientName);
    }

    private int getOrderId(HttpServletRequest request) throws ParameterException {
        if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
            throw new ParameterException("order.id.not.[resent");
        return Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
    }

    private boolean isUserHasRightToRead(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & R)==R || (Authentication.checkAccess(request) & r)==r;
    }
}