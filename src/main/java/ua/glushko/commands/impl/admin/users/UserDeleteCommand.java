package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.authentification.Authentication.*;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;

/**
 * Delete exist user
 */
public class UserDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & D) == D) {    //user has rights to delete
                UsersService usersService = UsersService.getService();
                Integer userId = Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_USER_ID));
                LOGGER.debug("deleting user " + userId);
                // update user data into database
                User user = usersService.getUserById(userId);
                usersService.deleteUser(userId);
                LOGGER.debug("user " + user.getLogin() + " was deleted");
                request.setAttribute(PARAM_COMMAND, COMMAND_USERS);
            }
        } catch (TransactionException | PersistException | ParameterException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }
}
