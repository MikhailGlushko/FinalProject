package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.DatabaseException;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.authentification.Authentication.U;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS_READ;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.PARAM_USER_ID;
import static ua.glushko.services.Validator.getValidatedUserBeforeUpdateDetails;

/**
 * Update user data
 */
public class UserUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User userNew = new User();
        try {
            int access = Authentication.checkAccess(request);
            if ((access & U) == U) {
                userNew = getValidatedUserBeforeUpdateDetails(request);
                UsersService usersService = UsersService.getService();
                User userOld = usersService.getUserById(userNew.getId());
                populateUser(userNew, userOld);
                usersService.updateUser(userOld);
            }
            page = "/do?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_PAGE);
            return new CommandRouter(request, response, page);
        } catch (TransactionException | DatabaseException e) {
            LOGGER.error(e);
            page = "/do?command=" + COMMAND_USERS_READ + "&user_id=" + request.getParameter(PARAM_USER_ID);
            return new CommandRouter(request, response, page);
        } catch (ParameterException e) {
            LOGGER.error(e);
            request.setAttribute(PARAM_ERROR_MESSAGE,
                    MessageManager.getMessage(e.getMessage(), locale));
            page = "/do?command=" + COMMAND_USERS_READ + "&user_id=" + request.getParameter(PARAM_USER_ID);
            return new CommandRouter(request, response, page);
        }
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
