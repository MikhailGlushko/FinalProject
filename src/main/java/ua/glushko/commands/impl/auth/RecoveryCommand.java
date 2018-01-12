package ua.glushko.commands.impl.auth;

import org.apache.log4j.Logger;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.mail.MailThread;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

public class RecoveryCommand extends AbstractCommand {

    public static final String PARAM_NAME_MAIL_SETUP="mail_setup";
    private final Logger LOGGER = Logger.getLogger(RecoveryCommand.class.getSimpleName());

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            Properties properties = (Properties)request.getAttribute(PARAM_NAME_MAIL_SETUP);
            String login = request.getParameter(AbstractCommand.PARAM_NAME_USER_LOGIN);
            LOGGER.debug("user "+login+" try to recovery password");
            UsersService recoveryService = UsersService.getService();
            User userData = recoveryService.getUserByLogin(login);
            String text = "Secret key : "+request.getSession().getId();
            MailThread mailThread = new MailThread(userData.getEmail(), "Password reset", text ,properties);
            mailThread.start();
            LOGGER.debug("secret key for user "+login+" was sent to his email");
        } catch (PersistException | TransactionException | NullPointerException e) {
            LOGGER.error(e);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
    }
}