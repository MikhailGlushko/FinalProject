package ua.glushko.commands.impl.auth;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.mail.MailThread;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
/** Recovery password */
public class RecoveryCommand extends Command {

    public static final String PARAM_NAME_MAIL_SETUP="mail_setup";

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = null;
        try {
            locale = (String) request.getSession().getAttribute(PARAM_NAME_LOCALE);
            Properties properties = (Properties)request.getAttribute(PARAM_NAME_MAIL_SETUP);
            String userLogin = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN);
            LOGGER.debug("user "+userLogin+" try to recovery password");
            UsersService recoveryService = UsersService.getService();
            User userData = recoveryService.getUserByLogin(userLogin);
            String text = "Secret key : "+request.getSession().getId();
            MailThread mailThread = new MailThread(userData.getEmail(), "Password reset", text ,properties);
            mailThread.start();
            LOGGER.debug("secret key for user "+userLogin+" was sent to his email");
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_PASSWORD_WAS_SEND, locale));
        } catch (PersistException | TransactionException | NullPointerException e) {
            LOGGER.error(e);
            page = ConfigurationManager.getProperty(PATH_PAGE_RECOVER);
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_NOT_EXIST, locale));
            return new CommandRouter(request, response, page);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
        return new CommandRouter(request, response, page);
    }
}