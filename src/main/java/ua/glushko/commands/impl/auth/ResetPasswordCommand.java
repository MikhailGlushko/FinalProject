package ua.glushko.commands.impl.auth;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
public class ResetPasswordCommand extends AbstractCommand {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String userLogin     = request.getParameter(PARAM_NAME_USER_LOGIN);
        String userPassword  = request.getParameter(PARAM_NAME_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_NAME_USER_PASSWORD2);
        String userSecret    = request.getParameter(PARAM_NAME_USER_SECRET);
        String locale = (String) request.getSession().getAttribute(PARAM_NAME_LOCALE);
        try {
            UsersService resetPasswordServicee = UsersService.getService();
            User user = resetPasswordServicee.update(userLogin, userPassword, userPassword2, userSecret, request.getSession().getId());
            if(user!=null){
                LOGGER.debug(MessageManager.getMessage(MESSAGE_USER_IS_REGISTERED, locale));
                request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(MESSAGE_USER_PSSWORD_WAS_SEND, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            } else {
                request.setAttribute(PARAM_NAME_ERROR_MESSAGE,
                        MessageManager.getMessage(MESSAGE_USER_INCORRECT_DATA, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
            }
        } catch (SQLException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug(MessageManager.getMessage(MESSAGE_USER_ALREADY_EXIST, locale));
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(MESSAGE_USER_ALREADY_EXIST, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        }
        return new CommandRouter(request, response, page);
    }
}
