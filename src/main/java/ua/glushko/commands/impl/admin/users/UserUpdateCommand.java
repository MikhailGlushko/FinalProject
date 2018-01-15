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
import javax.servlet.http.HttpSession;

import static ua.glushko.authentification.Authentification.*;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_USERS;

/** Обновление данных о пользователе после редактирования */
public class UserUpdateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeUserDataToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_USERS+"&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeUserDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer userId = null;
        try {
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            userId = Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_ID));
            String userName = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_NAME);
            String userEmail = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_EMAIL);
            String userPhone = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PHONE);
            String userStatus = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_STATUS);
            String userRole = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_ROLE);
            UsersService usersService = UsersService.getService();
            // get user data drom database
            User user = usersService.getUserById(userId);
            user.setId(userId);
            user.setRole(userRole);
            user.setName(userName);
            user.setEmail(userEmail);
            user.setPhone(userPhone);
            user.setStatus(userStatus);
            if ((access & U) == U) {
                LOGGER.debug("updating user " + userId);
                // update user data into database
                usersService.updateUser(user);
                LOGGER.debug("user " + userId+" was updated");
            }
            session.setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_USERS);
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("user " + userId+" was not update");
            LOGGER.error(e);
        }
    }
}
