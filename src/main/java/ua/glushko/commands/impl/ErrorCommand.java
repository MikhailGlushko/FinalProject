package ua.glushko.commands.impl;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page = ConfigurationManager.getProperty(PATH_PAGE_ERROR);
        return new CommandRouter(request, response, page);
    }
}
