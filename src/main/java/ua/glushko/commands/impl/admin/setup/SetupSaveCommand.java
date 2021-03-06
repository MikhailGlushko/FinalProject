package ua.glushko.commands.impl.admin.setup;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;

import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.prepareUserDataBeforeSetup;

/**
 * Command which receiving data from the form and updating in in Database
 * @version 1.0
 * @author Mikhail Glushko
 * @see User
 * @see UsersService
 */

public class SetupSaveCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        UsersService usersService = UsersService.getService();
        User newUserdata;
        try {
            saveUserData(request, usersService);
            page = PARAM_SERVLET_PATH;
        } catch (SQLException | TransactionException e ){
            LOGGER.error(e);
            request.setAttribute(PARAM_ERROR_MESSAGE,MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_DATA, locale));
        } catch (ParameterException e) {
            LOGGER.error(e);
            request.setAttribute(PARAM_ERROR_MESSAGE,MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }

    private void saveUserData(HttpServletRequest request, UsersService usersService) throws ParameterException, SQLException, TransactionException {
        User newUserdata;
        newUserdata = prepareUserDataBeforeSetup(request);
        User oldUserData = usersService.getUserById(newUserdata.getId());
        populateUserData(newUserdata,oldUserData);
        usersService.updateUser(oldUserData);
        storeUserDataToSession(request,oldUserData);
    }

    private void storeUserDataToSession(HttpServletRequest request, User user){
        request.setAttribute(PARAM_COMMAND, COMMAND_USERS);
        request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
        request.getSession().setAttribute(UsersCommandHelper.PARAM_USER_LOGIN, user.getLogin());
        request.getSession().setAttribute(UsersCommandHelper.PARAM_USER_NAME, user.getName());
        request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_WELCOME);
    }

    private void populateUserData(User userFrom, User userTo){
        userTo.setLogin(userFrom.getLogin());
        userTo.setName(userFrom.getName());
        userTo.setPassword(userFrom.getPassword());
        userTo.setEmail(userFrom.getEmail());
        userTo.setPhone(userFrom.getPhone());
    }
}
