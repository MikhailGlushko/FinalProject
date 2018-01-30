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
        try {
            int access = Authentication.checkAccess(request);
            if ((access & R) == R || (access & r) == r) {
                if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
                    throw new ParameterException("order.id.not.[resent");
                int orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));

                OrdersService ordersService = OrdersService.getService();
                Order order = ordersService.getOrderById(orderId);
                List<String> titles = ordersService.getOrderTitles();

                UsersService usersService = UsersService.getService();
                User client = usersService.getUserById(order.getUserId());
                Integer clientId = client.getId();
                String clientName = client.getName();

                List<User> stuffs = usersService.getUsersByRole(UserRole.CUSTOMER, false);

                request.setAttribute(PARAM_ORDER_USER_ID, clientId);
                request.setAttribute(PARAM_ORDER_USER_NAME, clientName);
                request.setAttribute(OrdersCommandHelper.PARAM_ORDERS_LIST_TITLE, titles);
                request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
                request.setAttribute(OrdersCommandHelper.PARAM_EMPLOYEES_LIST, stuffs);

                RepairServicesService repairServices = RepairServicesService.getService();
                List<RepairService> repairServiceList = repairServices.getRepairServiceList();
                request.setAttribute(OrdersCommandHelper.PARAM_SERVICES_LIST, repairServiceList);
                request.setAttribute(PARAM_ORDERS_STAT, OrderStatus.values());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_DETAIL);
        return new CommandRouter(request, response, page);
    }
}