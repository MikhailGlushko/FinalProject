package ua.glushko.commands.impl.auth;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
/** Reset password */
public class ResetPasswordCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String userLogin     = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN);
        String userPassword  = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD);
        String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD2);
        String userSecret    = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_SECRET);
        String locale = (String) request.getSession().getAttribute(PARAM_NAME_LOCALE);
        try {
            UsersService resetPasswordService = UsersService.getService();
            User user = resetPasswordService.changePassword(userLogin, userPassword, userPassword2, userSecret, request.getSession().getId());
            if(user!=null){
                LOGGER.debug("New password for user : "+userLogin+" was changed.");
                request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_PASSWORD_WAS_CHANGED, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            } else {
                LOGGER.debug("User "+userLogin+" input incorrect data for new password.");
                request.setAttribute(PARAM_NAME_ERROR_MESSAGE,
                        MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_DATA, locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_RESET_PASSWORD);
            }
        } catch (SQLException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug("Password for user "+userLogin+" did not change.");
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_NOT_EXIST, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        }
        return new CommandRouter(request, response, page);
    }
}
