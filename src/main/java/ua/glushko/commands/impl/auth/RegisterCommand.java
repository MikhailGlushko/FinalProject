package ua.glushko.commands.impl.auth;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static ua.glushko.services.Validator.getValidatedUserBeforeRegistration;

/** register new User */
public class RegisterCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User newUser = new User();
        try {
            newUser = getValidatedUserBeforeRegistration(request);
            UsersService registerService = UsersService.getService();
            registerService.register(newUser.getLogin(), newUser.getPassword(), newUser.getName(), newUser.getEmail(), newUser.getPhone());
            LOGGER.debug("New user : "+newUser.getLogin()+" was registered.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (PersistException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug("User already exist :"+newUser.getLogin()+" Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        } catch (ParameterException e) {
            LOGGER.debug("User : "+newUser.getLogin()+" input incorrect data. Registration rejected.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        }
        return new CommandRouter(request, response, page);
    }
}
