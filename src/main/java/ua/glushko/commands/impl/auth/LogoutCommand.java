package ua.glushko.commands.impl.auth;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;

/** Logout */
public class LogoutCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("user "+ Authentication.getCurrentUserLogin(request.getSession())+" logout");
        request.getSession().invalidate();
        request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_LOGIN);
        return new CommandRouter(request, response, PARAM_SERVLET_PATH);
    }
}
