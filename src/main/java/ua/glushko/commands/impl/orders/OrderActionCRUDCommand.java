package ua.glushko.commands.impl.orders;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UserCreateCommand;
import ua.glushko.commands.impl.admin.users.UserDeleteCommand;
import ua.glushko.commands.impl.admin.users.UserUpdateCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;

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
        String action = request.getParameter("action");
        request.setAttribute(PARAM_PAGE,request.getParameter(PARAM_PAGE));
        switch (action) {
            case "save":
                page= "/do?command=" + COMMAND_ORDER_UPDATE;
                break;
            case "add":
                page= "/do?command=" + COMMAND_ORDER_CREATE;
                break;
            case "delete":
                page= "/do?command=" + COMMAND_ORDER_DELETE;
                break;
        }
        return new CommandRouter(request, response, page);
    }
}