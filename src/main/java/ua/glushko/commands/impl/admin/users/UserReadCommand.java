package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
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
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.PATH_PAGE_USERS_DETAIL;

/** Отображение информации о пользователе с возможностью редактирования или удаления*/
public class UserReadCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeUserDetailsToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_USERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeUserDetailsToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try {
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            Integer id = Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_NAME_USER_ID));
            UsersService usersService = UsersService.getService();
            if ((access & U) == U) {
                User user = usersService.getUserById(id);
                List<String> titles = usersService.getUsersTitles();
                session.setAttribute(UsersCommandHelper.PARAM_NAME_USER_LIST_TITLE, titles);
                session.setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
            }
        } catch (NumberFormatException e){
            LOGGER.error(e);
        }
    }
}