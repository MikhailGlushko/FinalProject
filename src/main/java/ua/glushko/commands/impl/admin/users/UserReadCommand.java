package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.authentification.Authentication.*;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.PATH_PAGE_USERS_DETAIL;

/**
 * Show user information
 */
public class UserReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & U) == U) {    //user has rights to update
                Integer id = Integer.valueOf(request.getParameter(UsersCommandHelper.PARAM_USER_ID));
                UsersService usersService = UsersService.getService();
                User user = usersService.getUserById(id);
                List<String> titles = usersService.getUsersTitles();
                request.setAttribute(UsersCommandHelper.PARAM_USER_LIST_TITLE, titles);
                request.setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
            }
        } catch (PersistException | ParameterException e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_USERS_DETAIL);
        return new CommandRouter(request, response, page);
    }
}