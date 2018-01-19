package ua.glushko.commands.impl.auth;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.*;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static ua.glushko.services.Validator.isUserStatusActive;
import static ua.glushko.services.Validator.isUserStatusNotActive;
import static ua.glushko.services.Validator.getValidatedUserBeforeLogin;

/** User authorization */
public class LoginCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User userBeforeLogin = new User();
        try {
            userBeforeLogin = getValidatedUserBeforeLogin(request);
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " try to login");
            UsersService loginService = UsersService.getService();
            Map<User, List<Grant>> userAuthenticateData = loginService.authenticateUser(userBeforeLogin.getLogin(), userBeforeLogin.getPassword());
            User userAfterLogin = userAuthenticateData.keySet().iterator().next();
            List<Grant> currentUserGrants = userAuthenticateData.get(userAfterLogin);
            if (isUserStatusActive(userAfterLogin)) {
                LOGGER.debug("user " + userAfterLogin.getLogin() + " was login");
                storeUserAuthenticateData(request.getSession(), userAfterLogin, currentUserGrants);
                page = ConfigurationManager.getProperty(PATH_PAGE_MAIN);
            } else if (isUserStatusNotActive(userAfterLogin)) {
                LOGGER.debug("user " + userAfterLogin.getLogin() + " is " + userAfterLogin.getStatus());
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_STATUS + userAfterLogin.getStatus(), locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            }
        } catch (PersistException | TransactionException e) {
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (ParameterException e) {
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            LOGGER.debug(MessageManager.getMessage(e.getMessage()));
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        }
        return new CommandRouter(request, response, page);
    }

    private void storeUserAuthenticateData(HttpSession session, User currentUser, List<Grant> userGrants) {
        session.setAttribute(Authentification.PARAM_LOGIN, currentUser.getLogin());
        session.setAttribute(Authentification.PARAM_NAME_NAME, currentUser.getName());
        session.setAttribute(Authentification.PARAM_ROLE, currentUser.getRole());
        session.setAttribute(Authentification.PARAM_ID, currentUser.getId());
        session.setAttribute(Authentification.PARAM_GRANTS, userGrants);
    }
}
