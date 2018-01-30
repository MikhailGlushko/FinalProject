package ua.glushko.commands.impl.orders;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;

/**
 * Admin Order Management Command that receives data from a form and analyzes what operation it must perform on the data
 * @version 1.0
 * @author Mikhail Glushko
 * @see OrderCreateCommand
 * @see OrderDeleteCommand
 * @see OrderUpdateCommand
 */
public class OrderActionCRUDCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String action = request.getParameter(PARAM_ORDER_ACTION);
        request.setAttribute(PARAM_PAGE,request.getParameter(PARAM_PAGE));
        switch (action) {
            case PARAM_ORDER_ACTION_SAVE:
                page= PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDER_UPDATE;
                break;
            case PARAM_ORDER_ACTION_ADD:
                page= PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDER_CREATE;
                break;
            case PARAM_ORDER_ACTION_DELETE:
                page= PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDER_DELETE;
                break;
        }
        return new CommandRouter(request, response, page);
    }
}
