package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
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
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.prepareUserDataBeforeCreate;

/**
 * Admin User Management Command, which receives data from the form and creates a new record
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class UserCreateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_ADD);
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        UsersService usersService = UsersService.getService();
        User newUser = null;
        try {
            newUser = prepareUserDataBeforeCreate(request);
            usersService.updateUser(newUser);
            soreLastPageData(request, usersService);
            LOGGER.debug("New user : "+newUser.getLogin()+" was registered.");
            page = PARAM_SERVLET_PATH + "?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_LAST_PAGE);
        } catch (SQLException | TransactionException e) {
            LOGGER.debug("User already exist :"+newUser.getLogin()+" Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
        } catch (ParameterException e) {
            LOGGER.debug("Incorrect data. Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }

    private void soreLastPageData(HttpServletRequest request, UsersService usersService) throws SQLException {
        int count = usersService.count();
        Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        request.setAttribute(PARAM_LAST_PAGE,count);
        request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
    }

}
