package ua.glushko.commands.impl.auth;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** Logout */
public class LogoutCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("user "+ Authentication.getCurrentUserLogin(request.getSession())+" logout");
        request.getSession().invalidate();
        request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_LOGIN);
        String page ="/do";
        return new CommandRouter(request, response, page);
    }
}
