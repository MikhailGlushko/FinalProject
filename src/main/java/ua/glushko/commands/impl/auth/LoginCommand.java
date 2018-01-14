package ua.glushko.commands.impl.auth;

import org.apache.log4j.Logger;
import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.*;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
/** Авторизация пользователя */
public class LoginCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String locale = (String) request.getSession().getAttribute(PARAM_NAME_LOCALE);
        String currentUserLogin    = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_LOGIN);
        String currentUserPassword = request.getParameter(UsersCommandHelper.PARAM_NAME_USER_PASSWORD);
        try {
            LOGGER.debug("user "+currentUserLogin+" try to login");
            UsersService loginService = UsersService.getService();
            Map<User, List<Grant>> userAuthenticateData = loginService.authenticateUser(currentUserLogin, currentUserPassword);
            if (isUserActive(getCurrentUser(userAuthenticateData))) {
                saveAttributesToSession(request.getSession(), getCurrentUser(userAuthenticateData), getCurrentUserGrants(userAuthenticateData));
                page = ConfigurationManager.getProperty(PATH_PAGE_MAIN);
            } else if (isUserNotActive(getCurrentUser(userAuthenticateData))) {
                request.setAttribute(PARAM_NAME_ERROR_MESSAGE,
                        MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_STATUS + getCurrentUser(userAuthenticateData).getStatus(), locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            }
            LOGGER.debug("user "+currentUserLogin+" was login");
        } catch (PersistException | TransactionException | NullPointerException e) {
            LOGGER.debug("user "+currentUserLogin + " rejected");
            LOGGER.error(e);
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE,
                    MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        }
        return new CommandRouter(request, response, page);
    }

    private List<Grant> getCurrentUserGrants(Map<User, List<Grant>> useDataAndGrantsSet) throws NullPointerException {
        try {
            return useDataAndGrantsSet.get(useDataAndGrantsSet.keySet().iterator().next());
        } catch (NullPointerException e) {
            LOGGER.error(e);
            throw new NullPointerException("useDataAndGrantsSet is null");
        }
    }

    private User getCurrentUser(Map<User, List<Grant>> useDataAndGrantsSet) throws NullPointerException {
        try {
            return useDataAndGrantsSet.keySet().iterator().next();
        } catch (NullPointerException e) {
            LOGGER.error(e);
            throw new NullPointerException("useDataAndGrantsSet is null");
        }
    }

    private boolean isUserNotActive(User currentUser) throws NullPointerException {
        try {
            return currentUser != null && currentUser.getStatus() != UserStatus.ACTIVE;
        } catch (NullPointerException e) {
            LOGGER.error(e);
            throw new NullPointerException("currentUser is null");
        }
    }

    private boolean isUserActive(User currentUser) throws NullPointerException {
        try {
            return currentUser != null && currentUser.getStatus() == UserStatus.ACTIVE;
        } catch (NullPointerException e) {
            LOGGER.error(e);
            throw new NullPointerException("currentUser is null");
        }
    }

    private void saveAttributesToSession(HttpSession session, User currentUser, List<Grant> userGrants) throws NullPointerException {
        try {
            session.setAttribute(Authentification.PARAM_NAME_LOGIN, currentUser.getLogin());
            session.setAttribute(Authentification.PARAM_NAME_NAME, currentUser.getName());
            session.setAttribute(Authentification.PARAM_NAME_ROLE, currentUser.getRole());
            session.setAttribute(Authentification.PARAM_NAME_ID, currentUser.getId());
            session.setAttribute(Authentification.PARAM_NAME_GRANTS, userGrants);
        } catch (NullPointerException e) {
            LOGGER.debug(e);
            throw new NullPointerException("some parameters of userGrants is null");
        }
    }
}
