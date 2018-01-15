package ua.glushko.commands.impl.admin.history;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UserCreateCommand;
import ua.glushko.commands.impl.admin.users.UserDeleteCommand;
import ua.glushko.commands.impl.admin.users.UserUpdateCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;

/**
 * Анализ полученой комманды и перенаправление на соответствующую комманду
 * /do?command=<command>&action=<action>
 *
 * @see UserCreateCommand
 * @see UserUpdateCommand
 * @see UserDeleteCommand
 */
public class OrderHistoryActionCRUDCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page = getActionAndPrepareCommand(request);
        return new CommandRouter(request, response, page);

    }

    private String getActionAndPrepareCommand(HttpServletRequest request) {
        try {
            String action = request.getParameter("order_change_action");
            switch (action) {
                case "CHANGE_EMPLOYEE":
                    return "/do?command=" + COMMAND_NAME_HISTORY_CHANGE_EMPLOYEE;
                case "CHANGE_STATUS":
                    return "/do?command=" + COMMAND_NAME_HISTORY_CHANGE_STATUS;
                case "CHANGE_DATE":
                    return "/do?command=" + COMMAND_NAME_HISTORY_CHANGE_DATE;
                case "CHANGE_PRICE":
                    return "/do?command=" + COMMAND_NAME_HISTORY_CHANGE_PRICE;
                case "CHANGE_COMMENT":
                    return "/do?command=" + COMMAND_NAME_HISTORY_CHANGE_COMMENT;
                default:
                    return "/do?command=" +COMMAND_NAME_ORDERS_READ;
            }
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
