package ua.glushko.commands.impl.orders;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UserCreateCommand;
import ua.glushko.commands.impl.admin.users.UserDeleteCommand;
import ua.glushko.commands.impl.admin.users.UserUpdateCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;

/**
 * Analysis of the received command and redirection to the appropriate command
 * /do?command=<command>&action=<action>
 *     @see UserCreateCommand
 *     @see UserUpdateCommand
 *     @see UserDeleteCommand
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
