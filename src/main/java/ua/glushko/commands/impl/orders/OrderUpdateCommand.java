package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.utils.Authentication.u;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/**
 * Admin Order Management Command, which receives data from the form and update item in Database
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrderUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        OrdersService service = OrdersService.getService();
        try {
            if(isUserHasRightToUpdate(request)){
                updateOrder(request, service);
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void updateOrder(HttpServletRequest request, OrdersService service) throws SQLException, ParameterException, TransactionException {
        Order order = OrdersCommandHelper.getValidatedOrderBeforeUpdate(request);
        service.updateOrder(order);
        LOGGER.debug("order " + order.getId() + " was updated");
        request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
    }

    private boolean isUserHasRightToUpdate(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & U)==U || (Authentication.checkAccess(request) & u)==u;

    }
}
