package ua.glushko.commands.impl.auth;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.utils.mail.MailThread;
import ua.glushko.model.entity.User;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.prepareUserDataForRecoveryPassword;

/**
 * User Password Recovery Command
 * @version 1.0
 * @author Mikhail Glushko
 * @see User
 * @see UsersService
 */
public class RecoveryCommand implements Command {

    public static final String PARAM_MAIL_SETUP ="mail_setup";

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(PATH_PAGE_RECOVER);
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        UsersService recoveryService = UsersService.getService();
        User userBeforeRecovery = null;
        try {
            userBeforeRecovery = prepareUserDataForRecoveryPassword(request);
            LOGGER.debug("user "+userBeforeRecovery.getLogin()+" try to recovery password");
            User userData = recoveryService.getUserByLogin(userBeforeRecovery.getLogin());
            if(Objects.isNull(userData)){
                LOGGER.debug("Password for user " + userBeforeRecovery.getLogin() + " was not change. User not exist.");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_NOT_EXIST, locale));
            } else {
                String text = "Secret key : " + request.getSession().getId();
                Properties properties = (Properties)request.getAttribute(PARAM_MAIL_SETUP);
                MailThread mailThread = new MailThread(userData.getEmail(), "Password reset", text, properties);
                mailThread.start();
                LOGGER.debug("secret key for user " + userBeforeRecovery.getLogin() + " was sent to his email");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_PASSWORD_WAS_SEND, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
            }
        } catch (ParameterException e) {
            LOGGER.debug(e.getMessage());
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
        } catch (SQLException e) {
            LOGGER.debug("Password for user "+userBeforeRecovery.getLogin()+" was not change. User not exist.");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }
}