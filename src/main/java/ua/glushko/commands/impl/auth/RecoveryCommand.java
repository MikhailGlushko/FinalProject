package ua.glushko.commands.impl.auth;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.exception.DaoException;
import ua.glushko.utils.mail.MailThread;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.getValidatedUserBeforeRecoveryPassword;

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
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User user = new User();
        try {
            user = getValidatedUserBeforeRecoveryPassword(request);
            Properties properties = (Properties)request.getAttribute(PARAM_MAIL_SETUP);
            LOGGER.debug("user "+user.getLogin()+" try to recovery password");
            UsersService recoveryService = UsersService.getService();
            User userData = recoveryService.getUserByLogin(user.getLogin());
            if(Objects.nonNull(userData)) {
                String text = "Secret key : " + request.getSession().getId();
                MailThread mailThread = new MailThread(userData.getEmail(), "Password reset", text, properties);
                mailThread.start();
                LOGGER.debug("secret key for user " + user.getLogin() + " was sent to his email");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_PASSWORD_WAS_SEND, locale));
            } else {
                LOGGER.debug("Password for user " + user.getLogin() + " was not change. User not exist.");
                page = ConfigurationManager.getProperty(PATH_PAGE_RECOVER);
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_NOT_EXIST, locale));
                return new CommandRouter(request, response, page);
            }
        } catch (ParameterException | SQLException e) {
            LOGGER.debug("Password for user "+user.getLogin()+" was not change. User not exist.");
            page = ConfigurationManager.getProperty(PATH_PAGE_RECOVER);
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            return new CommandRouter(request, response, page);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
        return new CommandRouter(request, response, page);
    }
}