package ua.glushko.commands.impl.admin.users;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.authentification.Authentication.*;

/**
 * Show users list
 */
public class UsersListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & R) == R) {    //user has rights to read
                UsersService usersService = UsersService.getService();
                Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
                Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                String parameter = request.getParameter(PARAM_PAGE);
                Integer pageNumber;
                if (parameter == null || parameter.isEmpty() || parameter.equals("null"))
                    pageNumber = 1;
                else
                    pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
                LOGGER.debug("getting users list to how");
                List<User> users = usersService.getUsersList(pageNumber, pagesCount, rowsCount);
                List<String> titles = usersService.getUsersTitles();
                int count = usersService.count();
                count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
                request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
                request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
                request.setAttribute(PARAM_PAGE, pageNumber);
                request.setAttribute(PARAM_ACCESS, access);
                request.setAttribute(UsersCommandHelper.PARAM_USER_LIST_TITLE, titles);
                request.setAttribute(UsersCommandHelper.PARAM_USER_LIST, users);
                request.setAttribute(PARAM_LAST_PAGE, count);
                LOGGER.debug("users list were received and try to show");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS);
        return new CommandRouter(request, response, page);
    }
}