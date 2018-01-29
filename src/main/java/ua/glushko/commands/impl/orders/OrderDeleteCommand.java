package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.D;
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
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void deleteOrderDataFromDatabase(HttpServletRequest request) throws TransactionException, DatabaseException {
        Integer id;
        Order order;
        try {
            int access = Authentication.checkAccess(request);
            OrdersService ordersService = OrdersService.getService();
            if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
                throw new ParameterException("order.id.not.present");
            id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting order " + id);
                // update user data into database
                order = ordersService.getOrderById(id);
                ordersService.deleteOrder(id);
                LOGGER.debug("service "+order+" was deleted");
            } else {
                LOGGER.debug("user.don't.have.rights.to.delete");
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (ParameterException e) {
            LOGGER.debug("order did not delete");
            LOGGER.error(e);
        }
    }
}
