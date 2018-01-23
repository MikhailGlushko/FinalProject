package ua.glushko.commands.impl.auth;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.*;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static ua.glushko.services.utils.Validator.isUserStatusActive;
import static ua.glushko.services.utils.Validator.isUserStatusNotActive;
import static ua.glushko.services.utils.Validator.getValidatedUserBeforeLogin;

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
                storeUserAuthenticateData(request, userAfterLogin, currentUserGrants);
                request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_WELCOME);
                page = "/do";
            } else if (isUserStatusNotActive(userAfterLogin)) {
                LOGGER.debug("user " + userAfterLogin.getLogin() + " is " + userAfterLogin.getStatus());
                request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_STATUS + userAfterLogin.getStatus(), locale));
                page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
            }
        } catch (DaoException | TransactionException e) {
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (DatabaseException e){
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_DATABASE_NOT_FOUND, locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        } catch (ParameterException e) {
            LOGGER.debug("user " + userBeforeLogin.getLogin() + " rejected");
            LOGGER.debug(MessageManager.getMessage(e.getMessage()));
            request.setAttribute(PARAM_ERROR_MESSAGE, MessageManager.getMessage(e.getMessage(), locale));
            page = ConfigurationManager.getProperty(PATH_PAGE_LOGIN);
        }
        return new CommandRouter(request, response, page);
    }

    private void storeUserAuthenticateData(HttpServletRequest request, User currentUser, List<Grant> userGrants) {
        request.getSession().setAttribute(Authentication.PARAM_LOGIN, currentUser.getLogin());
        request.getSession().setAttribute(Authentication.PARAM_NAME_NAME, currentUser.getName());
        request.getSession().setAttribute(Authentication.PARAM_ROLE, currentUser.getRole());
        request.getSession().setAttribute(Authentication.PARAM_ID, currentUser.getId());
        request.getSession().setAttribute(Authentication.PARAM_GRANTS, userGrants);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies) {
            String name = cookie.getName();
            if (name.equals(PARAM_LOCALE)) {
                request.getSession().setAttribute(PARAM_LOCALE, cookie.getValue());
                break;
            }
        }
    }
}
