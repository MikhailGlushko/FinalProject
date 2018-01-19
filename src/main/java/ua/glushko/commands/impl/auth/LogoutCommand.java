package ua.glushko.commands.impl.auth;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** Logout */
public class LogoutCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("user "+ Authentification.getCurrentUserLogin(request.getSession())+" logout");
        request.getSession().invalidate();
        String page = ConfigurationManager.getProperty(PATH_PAGE_INDEX);
        return new CommandRouter(request, response, page);
    }
}
