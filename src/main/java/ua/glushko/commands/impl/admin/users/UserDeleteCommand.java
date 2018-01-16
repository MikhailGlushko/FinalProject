package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.authentification.Authentification.*;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_USERS;

/** Delete exist user */
public class UserDeleteCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            deleteUserDataFromDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_USERS + "&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void deleteUserDataFromDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer userId = null;
        try {
            int access = Authentification.checkAccess(request);
            UsersService usersService = UsersService.getService();
            userId = Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting user " + userId);
                // update user data into database
                User user = usersService.getUserById(userId);
                usersService.deleteUser(userId);
                LOGGER.debug("user "+user.getLogin()+" was deleted");
            }
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_USERS);
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("user "+userId+" did not delete");
            LOGGER.error(e);
        }
    }
}
