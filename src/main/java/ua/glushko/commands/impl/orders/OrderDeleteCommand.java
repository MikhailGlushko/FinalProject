package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.D;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/**
 * delete exist order
 */
public class OrderDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            int access = Authentication.checkAccess(request);
            if ((access & D) == D) {
                OrdersService ordersService = OrdersService.getService();
                if (Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
                    throw new ParameterException("order.id.not.present");
                Integer id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
                LOGGER.debug("deleting order " + id);
                // update user data into database
                Order order = ordersService.getOrderById(id);
                ordersService.deleteOrder(id);
                LOGGER.debug("service " + order + " was deleted");
            } else {
                LOGGER.debug("user.don't.have.rights.to.delete");
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }
}
