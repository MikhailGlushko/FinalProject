package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_ADD;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.C;
import static ua.glushko.commands.utils.Authentication.c;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/**
 * Admin Order Management Command, which receives data from the form and creates a new record
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrderCreateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page;
        try {
            int access = Authentication.checkAccess(request);
            if ((access & C) == C || (access & c)==c) {
                Order order = OrdersCommandHelper.getValidatedOrderBeforeCreate(request);
                LOGGER.debug("creating new order "+order);

                OrdersService.getService().updateOrder(order);
                LOGGER.debug("new order "+order+" was created");
                int count = OrdersService.getService().count(order.getUserId());
                Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                request.setAttribute(PARAM_LAST_PAGE,count);
            }
            page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_LAST_PAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            request.setAttribute(PARAM_COMMAND,COMMAND_ORDERS_ADD);
            page = PARAM_SERVLET_PATH;
        }
        return new CommandRouter(request, response, page);
    }

}
