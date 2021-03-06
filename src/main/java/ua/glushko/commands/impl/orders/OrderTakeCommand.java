package ua.glushko.commands.impl.orders;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.entity.UserRole;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.commands.utils.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_READ;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.PARAM_ID;
import static ua.glushko.commands.utils.Authentication.PARAM_ROLE;
import static ua.glushko.commands.utils.Authentication.U;

/**
 * Admin Order Management Command, which find new oldest order wint status NEW and assign it to current employee with role MANAGER
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrderTakeCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            Order order = takeNextOneNewOrderWithoutEmployee(request);
            if (Objects.nonNull(order))
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS_READ + "&" + OrdersCommandHelper.PARAM_ORDER_ID + "=" + order.getId();
            else
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS;
        } catch (Exception e) {
            LOGGER.error(e);
            page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS;
        }
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
    }

    private Order takeNextOneNewOrderWithoutEmployee(HttpServletRequest request) throws TransactionException, SQLException {
        Order order = null;
        OrdersService ordersService = OrdersService.getService();
        try {
            if(isManagerRightToUpdate(request)){
                order = getNewOrder(request, ordersService);
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (ParameterException e) {
            LOGGER.error(e);
        }
        return order;
    }

    private Order getNewOrder(HttpServletRequest request, OrdersService ordersService) throws SQLException, TransactionException {
        Order order;
        Integer userId = (Integer) request.getSession().getAttribute(PARAM_ID);
        order = ordersService.takeNewOrder(userId, OrderStatus.NEW);
        return order;
    }

    private boolean isManagerRightToUpdate(HttpServletRequest request) throws ParameterException {
        UserRole role = (UserRole) request.getSession().getAttribute(PARAM_ROLE);
        return (Authentication.checkAccess(request) & U)==U && role == UserRole.MANAGER;

    }
}
