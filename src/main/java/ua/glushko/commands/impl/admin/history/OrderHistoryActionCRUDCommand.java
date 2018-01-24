package ua.glushko.commands.impl.admin.history;

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
 *
 * @see UserCreateCommand
 * @see UserUpdateCommand
 * @see UserDeleteCommand
 */
public class OrderHistoryActionCRUDCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String action = request.getParameter("order_change_action");
        switch (action) {
            case "CHANGE_EMPLOYEE":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_EMPLOYEE;
                break;
            case "CHANGE_STATUS":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_STATUS;
                break;
            case "CHANGE_DATE":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_DATE;
                break;
            case "CHANGE_PRICE":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_PRICE;
                break;
            case "CHANGE_COMMENT":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_COMMENT;
                break;
            case "GUESTBOOK_COMMENT":
                page= "/do?command=" + COMMAND_HISTORY_CHANGE_GUEST_BOOK;
                break;
            default:
                page= "/do?command=" + COMMAND_ORDERS_READ;
        }
        return new CommandRouter(request, response, page);
    }
}
