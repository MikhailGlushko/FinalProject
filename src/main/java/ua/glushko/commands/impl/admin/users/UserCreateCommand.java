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

/** Создание нового пользователя */
public class UserCreateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeUserDataToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_USERS + "&page=" + request.getSession().getAttribute(PARAM_NAME_PAGES_COUNT);
        return new CommandRouter(request, response, page);

    }

    private void storeUserDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        String userLogin = null;
        try {
            int access = Authentification.checkAccess(request);
            userLogin = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN);
            String userPassword = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD);
            String userName = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_NAME);
            String userEmail = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_EMAIL);
            String userPhone = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PHONE);
            String userStatus = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_STATUS);
            String userRole = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_ROLE);
            UsersService usersService = UsersService.getService();
            User user = new User();
            user.setLogin(userLogin);
            user.setPassword(userPassword);
            user.setRole(userRole);
            user.setName(userName);
            user.setEmail(userEmail);
            user.setPhone(userPhone);
            user.setStatus(userStatus);
            if ((access & C) == C) {
                LOGGER.debug("creating new user "+userLogin);
                // update user data into database
                usersService.updateUser(user);
                LOGGER.debug("new user "+userLogin+" was created");
            }
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("new user "+userLogin+" did not create");
            LOGGER.error(e);
        }
    }
}
