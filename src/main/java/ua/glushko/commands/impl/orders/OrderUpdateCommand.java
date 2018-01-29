package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.utils.Authentication.u;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/** Update data after editing */
public class OrderUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            int access = Authentication.checkAccess(request);
            Order item = OrdersCommandHelper.getValidatedOrderBeforeUpdate(request);
            if ((access & U) == U || (access & u) == u) {
                OrdersService.getService().updateOrder(item);
            }
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, item);
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
            LOGGER.debug("order " + item.getId()+" was updated");
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS +"&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);

    }
}
