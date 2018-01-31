package ua.glushko.commands.impl.admin.setup;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.User;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Command for display details of the current the user
 * @version 1.0
 * @author Mikhail Glushko
 * @see User
 * @see UsersService
 */
public class SetupCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
        UsersService usersService = UsersService.getService();
        try {
            String userLogin = getLogin(request);
            User user = usersService.getUserByLogin(userLogin);
            storeUserData(request, user);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new CommandRouter(request, response, page);
    }

    private void storeUserData(HttpServletRequest request, User user) {
        request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
    }

    private String getLogin(HttpServletRequest request) throws ParameterException {
        if (Objects.isNull(request.getSession().getAttribute(Authentication.PARAM_LOGIN)))
            throw new ParameterException("user.didn't.login");
        return (String) request.getSession().getAttribute(Authentication.PARAM_LOGIN);
    }
}