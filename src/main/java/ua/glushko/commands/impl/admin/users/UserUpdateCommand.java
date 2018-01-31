package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS_READ;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.PARAM_USER_ID;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.getUserDataBeforeUpdate;

/**
 * Admin User Management Command, which receives data from the form and update item in Database
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class UserUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS_READ + "&user_id=" + request.getParameter(PARAM_USER_ID);
        UsersService usersService = UsersService.getService();
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        try {
            if(isUserHasRightToUpdate(request)){
                User newUser = getUserDataBeforeUpdate(request);
                User oldUser = usersService.getUserById(newUser.getId());
                populateUser(newUser, oldUser);
                usersService.updateUser(oldUser);
            }
            page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_PAGE);
        } catch (TransactionException | SQLException e) {
            LOGGER.error(e);
        } catch (ParameterException e) {
            LOGGER.error(e);
            request.setAttribute(PARAM_ERROR_MESSAGE,MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }

    private boolean isUserHasRightToUpdate(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & U) == U;
    }

    private void populateUser(User userFrom, User userTo) {
        userTo.setId(userFrom.getId());
        userTo.setName(userFrom.getName());
        userTo.setEmail(userFrom.getEmail());
        userTo.setPhone(userFrom.getPhone());
        userTo.setRole(userFrom.getRole());
        userTo.setStatus(userFrom.getStatus());
        userTo.setPassword(null);
    }
}
