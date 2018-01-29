package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;

/**
 * Analysis of the received command and redirection to the appropriate command
 * /do?command=<command>&action=<action>
 *
 * @see UserCreateCommand
 * @see UserUpdateCommand
 * @see UserDeleteCommand
 */
public class UserActionCRUDCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String action = request.getParameter("action");
        request.setAttribute(PARAM_PAGE, request.getParameter(PARAM_PAGE));
        switch (action) {
            case "save":
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS_UPDATE;
                break;
            case "add":
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS_CREATE;
                break;
            case "delete":
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS_DELETE;
        }
        return new CommandRouter(request, response, page);
    }
}
