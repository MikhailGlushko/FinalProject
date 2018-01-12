package ua.glushko.commands.impl.auth;

import org.apache.log4j.Logger;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand extends AbstractCommand {
    private final Logger LOGGER = Logger.getLogger(LogoutCommand.class.getSimpleName());
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("user was logout");
        request.getSession().invalidate();
        String page = ConfigurationManager.getProperty(PATH_PAGE_INDEX);
        return new CommandRouter(request, response, page);
    }
}
