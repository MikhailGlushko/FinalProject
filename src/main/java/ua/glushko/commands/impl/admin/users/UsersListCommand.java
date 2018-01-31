package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.User;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.commands.utils.Authentication.*;

/**
 * Admin User Management Command, which prepare list of users to show
 * @author Mikhail Glushko
 * @version 1.0
 * @see User
 * @see UsersService
 */
public class UsersListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        UsersService usersService = UsersService.getService();
        try {
            if(isUserHasRightToread(request)){
                Integer pageNumber = getPageNumber(request);
                LOGGER.debug("getting users list to how");
                List<User> users = usersService.getUsersList(pageNumber);
                List<String> titles = usersService.getUsersTitles();
                int count = usersService.count();
                storeUserData(request, pageNumber, users, titles, count);
                LOGGER.debug("users list were received and try to show");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS);
        return new CommandRouter(request, response, page);
    }

    private void storeUserData(HttpServletRequest request, Integer pageNumber, List<User> users, List<String> titles, int count) throws ParameterException {
        Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, Authentication.checkAccess(request));
        request.setAttribute(UsersCommandHelper.PARAM_USER_LIST_TITLE, titles);
        request.setAttribute(UsersCommandHelper.PARAM_USER_LIST, users);
        request.setAttribute(PARAM_LAST_PAGE, count);
    }

    private Integer getPageNumber(HttpServletRequest request) {
        Integer pageNumber;
        String parameter = request.getParameter(PARAM_PAGE);
        if (parameter == null || parameter.isEmpty() || parameter.equals("null"))
            pageNumber = 1;
        else
            pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
        return pageNumber;
    }

    private boolean isUserHasRightToread(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & R) == R;
    }
}