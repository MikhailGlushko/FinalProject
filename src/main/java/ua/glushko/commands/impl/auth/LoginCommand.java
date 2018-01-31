package ua.glushko.commands.impl.auth;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.*;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Validator.isUserStatusActive;
import static ua.glushko.commands.utils.Validator.isUserStatusNotActive;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.prepareUserDataForLogin;

/**
 * User authorization Command
 *
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class LoginCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        String page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);;
        UsersService loginService = UsersService.getService();
        User userBeforeLogin = null;
        try {
            userBeforeLogin = prepareUserDataForLogin(request);
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " try to login");
            Map<User, List<Grant>> userAuthenticateData = loginService.authenticateUser(userBeforeLogin.getLogin(), userBeforeLogin.getPassword());
            User userAfterLogin = getUserAfrerAuthenticate(userAuthenticateData);
            if (Objects.isNull(userAuthenticateData)) {
                LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD, locale));
            } else if (isUserStatusNotActive(userAfterLogin)) {
                LOGGER.debug("user " + userAfterLogin.getLogin() + " is " + userAfterLogin.getStatus());
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_STATUS + userAfterLogin.getStatus(), locale));
            } else if (isUserStatusActive(userAfterLogin)) {
                LOGGER.debug("user " + userAfterLogin.getLogin() + " was login");
                storeUserAuthenticateData(request, userAuthenticateData);
                page = PARAM_SERVLET_PATH;
            }
        } catch (SQLException | TransactionException e) {
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_DATABASE_NOT_FOUND, locale));
        } catch (ParameterException e) {
            LOGGER.debug(MessageManager.getMessage(e.getMessage()));
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
        }
        return new CommandRouter(request, response, page);
    }

    private List<Grant> getGrantsAfrerAuthenticate(Map<User, List<Grant>> userAuthenticateData) {
        if (Objects.nonNull(userAuthenticateData))
            return userAuthenticateData.get(userAuthenticateData.keySet().iterator().next());
        return null;
    }

    private User getUserAfrerAuthenticate(Map<User, List<Grant>> userAuthenticateData) {
        if (Objects.nonNull(userAuthenticateData))
            return userAuthenticateData.keySet().iterator().next();
        return null;
    }

    private void storeUserAuthenticateData(HttpServletRequest request, Map<User, List<Grant>> userAuthenticateData) {
        User currentUser = getUserAfrerAuthenticate(userAuthenticateData);
        request.getSession().setAttribute(Authentication.PARAM_LOGIN, currentUser.getLogin());
        request.getSession().setAttribute(Authentication.PARAM_NAME_NAME, currentUser.getName());
        request.getSession().setAttribute(Authentication.PARAM_ROLE, currentUser.getRole());
        request.getSession().setAttribute(Authentication.PARAM_ID, currentUser.getId());
        request.getSession().setAttribute(Authentication.PARAM_GRANTS, getGrantsAfrerAuthenticate(userAuthenticateData));
        request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_WELCOME);
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(PARAM_LOCALE)) {
                    request.getSession().setAttribute(PARAM_LOCALE, cookie.getValue());
                    break;
                }
            }
        }
    }
}
