package ua.glushko.commands.impl.orders;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.commands.utils.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_READ;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.utils.Authentication.u;

/**
 * Update data after editing
 */
public class OrderChangeStatusCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            int access = Authentication.checkAccess(request);
            Order order = getValidatedOrderBeforeChangeStatus(request);
            if ((access & U) == U || (access & u) == u) {
                OrdersService.getService().updateOrder(order);
            }
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
            request.setAttribute(PARAM_PAGE, request.getParameter(PARAM_PAGE));
        } catch (ParameterException | DatabaseException | TransactionException e) {
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS_READ);
            request.setAttribute(PARAM_PAGE, request.getParameter(PARAM_PAGE));
            request.setAttribute(PARAM_ORDER_ID, request.getParameter(PARAM_ORDER_ID));
        }
        return new CommandRouter(request, response, PARAM_SERVLET_PATH);
    }
}
