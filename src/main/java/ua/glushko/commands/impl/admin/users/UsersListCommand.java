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

/** Отображение списка пользователей*/
public class UsersListCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeUsersListToSession(request);
        }catch (TransactionException | PersistException e) {
           LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS);
        return new CommandRouter(request, response, page);
    }

    private void storeUsersListToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try{
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            UsersService usersService = UsersService.getService();
            Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            Integer rowsCount  = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter   = request.getParameter(PARAM_NAME_PAGE);
            Integer pageNumber;
            if(parameter==null || parameter.isEmpty())
                pageNumber = 1;
            else
                pageNumber = Integer.valueOf(request.getParameter(PARAM_NAME_PAGE));

            if ((access & R)== R) {
                LOGGER.debug("getting users list to how");
                List<User> users    = usersService.getUsersList(pageNumber, pagesCount, rowsCount);
                List<String> titles = usersService.getUsersTitles();
                int count = usersService.count();
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                session.setAttribute(PARAM_NAME_PAGES_COUNT, pagesCount);
                session.setAttribute(PARAM_NAME_ROWS_COUNT, rowsCount);
                session.setAttribute(PARAM_NAME_PAGE, pageNumber);
                session.setAttribute(PARAM_NAME_ACCESS,access);
                session.setAttribute(UsersCommandHelper.PARAM_NAME_USER_LIST_TITLE, titles);
                session.setAttribute(UsersCommandHelper.PARAM_NAME_USER_LIST, users);
                session.setAttribute(PARAM_NAME_LAST_PAGE,count);
                LOGGER.debug("users list were getted and try to show");
            }
        } catch (NullPointerException | NumberFormatException e){
            LOGGER.debug("users list were not get.");
            LOGGER.error(e);
        }
    }
}