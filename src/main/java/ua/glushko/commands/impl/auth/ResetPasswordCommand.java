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

import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.gprepareUserDataBeforeResetPassword;

/**
 * User Password Reset Command
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class ResetPasswordCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        UsersService resetPasswordService = UsersService.getService();
        User userBeforeResetPassword = null;
        try {
            userBeforeResetPassword = gprepareUserDataBeforeResetPassword(request);
            resetPasswordService.changePassword(userBeforeResetPassword.getLogin(), userBeforeResetPassword.getPassword());
            LOGGER.debug("New password for user : "+userBeforeResetPassword.getLogin()+" changed.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_PASSWORD_WAS_CHANGED, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (SQLException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug("Password for user "+userBeforeResetPassword.getLogin()+" was not change.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_NOT_EXIST, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        } catch (ParameterException e){
            LOGGER.debug(e.getMessage());
            request.setAttribute(PARAM_ERROR_MESSAGE,
                    MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
        }
        return new CommandRouter(request, response, page);
    }
}
