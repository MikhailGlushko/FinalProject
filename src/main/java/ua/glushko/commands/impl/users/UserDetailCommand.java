package ua.glushko.commands.impl.users;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.*;

/**
 * Отображение списка пользователей
 */
public class UserDetailCommand extends AbstractCommand {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            storeUserDetailsToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_USERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeUserDetailsToSession(HttpServletRequest request) throws PersistException, TransactionException {
        HttpSession session = request.getSession();
        List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
        UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
        String command = request.getParameter(PARAM_NAME_COMMAND);
        int access = Authentification.checkAccess(grantList, userRole, command);
        Integer id = Integer.valueOf(request.getParameter(PARAM_NAME_USER_ID));
        UsersService usersService = UsersService.getService();
        if ((access & U) == U) {
            User user = usersService.getUserById(id);
            List<String> titles = usersService.getUsersTitles();
            session.setAttribute(PARAM_NAME_USER_LIST_TITLE, titles);
            session.setAttribute(PARAM_NAME_USER, user);
        }
    }
}