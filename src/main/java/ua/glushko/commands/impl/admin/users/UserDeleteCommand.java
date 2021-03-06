package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.User;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.*;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;

/**
 * Admin User Management Command, which receives data from the form and delete record from database
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class UserDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        UsersService usersService = UsersService.getService();
        try {
            if(isUserHasRightToDelete(request)) {
                Integer userId = getUserId(request);
                LOGGER.debug("deleting user " + userId);
                User user = usersService.getUserById(userId);
                if(Objects.nonNull(user))
                    usersService.deleteUser(userId);
                LOGGER.debug("user " + user.getLogin() + " was deleted");
                request.setAttribute(PARAM_COMMAND, COMMAND_USERS);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private Integer getUserId(HttpServletRequest request) throws ParameterException {
        if(Objects.isNull(request.getParameter(UsersCommandHelper.PARAM_USER_ID)))
            throw new ParameterException("user.didn't.login");
        return Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_USER_ID));
    }

    private boolean isUserHasRightToDelete(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & D) == D;
    }
}
