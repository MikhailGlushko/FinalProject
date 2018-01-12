package ua.glushko.commands.impl;

import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WelcomeCommand extends AbstractCommand{

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(PATH_PAGE_WELCOME);
        return new CommandRouter(request, response, page);
    }
}
