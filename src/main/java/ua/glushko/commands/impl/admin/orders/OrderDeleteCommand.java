package ua.glushko.commands.impl.admin.orders;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.services.utils.Authentication.D;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/** delete exist order */
public class OrderDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            deleteOrderDataFromDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void deleteOrderDataFromDatabase(HttpServletRequest request) throws TransactionException, DatabaseException {
        Integer Id;
        Order order = null;
        try {
            int access = Authentication.checkAccess(request);
            OrdersService ordersService = OrdersService.getService();
            Id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting order " + Id);
                // update user data into database
                order = ordersService.getOrderById(Id);
                ordersService.deleteOrder(Id);
                LOGGER.debug("service "+order+" was deleted");
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (ParameterException e) {
            LOGGER.debug("order "+order+" did not delete");
            LOGGER.error(e);
        }
    }
}
