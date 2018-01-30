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
 * Display credentials to the user to change them
 */
public class SetupCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (Objects.isNull(request.getSession().getAttribute(Authentication.PARAM_LOGIN)))
                throw new ParameterException("user.not.logined");
            String userLogin = (String) request.getSession().getAttribute(Authentication.PARAM_LOGIN);
            UsersService usersService = UsersService.getService();
            User user = usersService.getUserByLogin(userLogin);
            request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
        return new CommandRouter(request, response, page);
    }
}