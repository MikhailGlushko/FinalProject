package ua.glushko.commands.impl.auth;

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
import java.util.Objects;

import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.prepareUserDataBeforeRegistration;

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
        String page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        UsersService registerService = UsersService.getService();
        User userBeforeRegistration = null;
        try {
            userBeforeRegistration = prepareUserDataBeforeRegistration(request);
            User user = registerService.register(userBeforeRegistration.getLogin(), userBeforeRegistration.getPassword(), userBeforeRegistration.getName(), userBeforeRegistration.getEmail(), userBeforeRegistration.getPhone());
            if(Objects.isNull(user)){
                LOGGER.debug("User already exist :" + userBeforeRegistration.getLogin() + " Registration rejected.");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
            } else {
                LOGGER.debug("New user : " + userBeforeRegistration.getLogin() + " was registered.");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            }
        } catch (SQLException | TransactionException e){
            LOGGER.error(e);
            LOGGER.debug(userBeforeRegistration.getLogin() + " Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_DATABASE_NOT_FOUND, locale));
        } catch (ParameterException e) {
            LOGGER.debug("User input incorrect data. Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }
}
