package ua.glushko.commands.impl.admin.setup;
import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Display credentials to the user to change them */
public class SetupCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        if(Authentication.isUserLogIn(request.getSession())) {
            String userLogin = (String) request.getSession().getAttribute(Authentication.PARAM_LOGIN);
            UsersService usersService = UsersService.getService();
            try {
                User user = usersService.getUserByLogin(userLogin);
                request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
            } catch (PersistException | TransactionException | ParameterException e) {
                LOGGER.error(e);
            }
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
        return new CommandRouter(request, response, page);
    }
}