package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ua.glushko.authentification.Authentification.*;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.commands.CommandFactory.COMMAND_USERS_ADD;
import static ua.glushko.services.Validator.getValidatedUserBeforeCreate;
import static ua.glushko.services.Validator.getValidatedUserBeforeRegistration;

/**
 * Creat new user
 */
public class UserCreateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User newUser = new User();
        try {
            newUser = getValidatedUserBeforeCreate(request);
            UsersService registerService = UsersService.getService();
            registerService.updateUser(newUser);
            int count = registerService.count();
            Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
            request.setAttribute(PARAM_LAST_PAGE,count);
            LOGGER.debug("New user : "+newUser.getLogin()+" was registered.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
            page = "/do?command=" + COMMAND_USERS + "&page=" + request.getAttribute(PARAM_LAST_PAGE);
        } catch (PersistException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug("User already exist :"+newUser.getLogin()+" Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
            //page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
            page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_ADD);
        } catch (ParameterException e) {
            LOGGER.debug("User : "+newUser.getLogin()+" input incorrect data. Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_ADD);
        }
        return new CommandRouter(request, response, page);
    }

}
