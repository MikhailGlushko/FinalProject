package ua.glushko.commands.impl.admin.users;
import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Display credentials to the user to change them */
public class SetupCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        if(Authentification.isUserLogIn(request.getSession())) {
            String userLogin = (String) request.getSession().getAttribute(Authentification.PARAM_NAME_LOGIN);
            UsersService usersService = UsersService.getService();
            try {
                User user = usersService.getUserByLogin(userLogin);
                request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
            } catch (PersistException | TransactionException e) {
                LOGGER.error(e);
            }
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
        return new CommandRouter(request, response, page);
    }
}