package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UserCreateCommand;
import ua.glushko.commands.impl.admin.users.UserDeleteCommand;
import ua.glushko.commands.impl.admin.users.UserUpdateCommand;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;

/**
 * Анализ полученой комманды и перенаправление на соответствующую комманду
 * /do?command=<command>&action=<action>
 *     @see UserCreateCommand
 *     @see UserUpdateCommand
 *     @see UserDeleteCommand
 */
public class ServiceActionCRUDCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page = getActionAndPrepareCommand(request);
        return new CommandRouter(request, response, page);

    }

    private String getActionAndPrepareCommand(HttpServletRequest request){
        try {
            String action = request.getParameter("action");
            switch (action) {
                case "save":
                    return "/do?command=" + COMMAND_NAME_SERVICES_UPDATE;
                case "add":
                    return "/do?command=" + COMMAND_NAME_SERVICES_CREATE;
                case "delete":
                    return "/do?command=" + COMMAND_NAME_SERVICES_DELETE;
            }
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
