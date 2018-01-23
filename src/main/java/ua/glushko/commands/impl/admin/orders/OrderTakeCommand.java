package ua.glushko.commands.impl.admin.orders;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.utils.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;
import static ua.glushko.services.utils.Authentication.PARAM_ID;
import static ua.glushko.services.utils.Authentication.U;

/** delete exist order */
public class OrderTakeCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            takeNextOneNewOrder(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void takeNextOneNewOrder(HttpServletRequest request) throws TransactionException, DatabaseException {
        Integer Id = null;
        Order order = null;
        try {
            int access = Authentication.checkAccess(request);
            OrdersService ordersService = OrdersService.getService();
            if ((access & U) == U) {
                Integer userId = (Integer) request.getSession().getAttribute(PARAM_ID);
                ordersService.takeNewOrder(userId);
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (ParameterException e) {
            LOGGER.debug("order "+order+" did not delete");
            LOGGER.error(e);
        }
    }
}
