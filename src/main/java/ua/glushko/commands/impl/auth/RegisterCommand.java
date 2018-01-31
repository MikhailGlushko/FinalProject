package ua.glushko.commands.impl.auth;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.Objects;

import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.getValidatedUserBeforeRegistration;

/**
 * New User Register Command
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class RegisterCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User newUser = new User();
        try {
            newUser = getValidatedUserBeforeRegistration(request);
            UsersService registerService = UsersService.getService();
            User user = registerService.register(newUser.getLogin(), newUser.getPassword(), newUser.getName(), newUser.getEmail(), newUser.getPhone());
            if(Objects.nonNull(user)) {
                LOGGER.debug("New user : " + newUser.getLogin() + " was registered.");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            } else {
                LOGGER.debug("User already exist :" + newUser.getLogin() + " Registration rejected.");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
            }
        } catch (SQLException | TransactionException e){
            LOGGER.error(e);
            LOGGER.debug("Database not found :" + newUser.getLogin() + " Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_DATABASE_NOT_FOUND, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        } catch (ParameterException e) {
            LOGGER.debug("User : "+newUser.getLogin()+" input incorrect data. Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        }
        return new CommandRouter(request, response, page);
    }
}
