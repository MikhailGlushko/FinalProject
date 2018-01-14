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
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
/** Регистрация пользователя (клиента)в системе*/
public class RegisterCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute(PARAM_NAME_LOCALE);

        UsersService registerService = UsersService.getService();
        String userLogin = null;
        try {
            userLogin = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN);
            String userPassword = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD);
            String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD2);
            String userName  = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_NAME);
            String userEmail = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_EMAIL);
            String userPhone = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PHONE);
            registerService.register(userLogin, userPassword, userPassword2, userName, userEmail, userPhone);
            LOGGER.debug("New user : "+userLogin+" was registered.");
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_IS_REGISTERED, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (SQLException | TransactionException e) {
            LOGGER.error(e);
            LOGGER.debug("User already exist :"+userLogin+" Registration rejected.");
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_ALREADY_EXIST, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        } catch (NullPointerException e) {
            LOGGER.debug("User : "+userLogin+" input incorrect data. Registration rejected.");
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_DATA, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_REGISTER);
        }
        return new CommandRouter(request, response, page);
    }
}
