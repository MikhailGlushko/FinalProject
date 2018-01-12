package ua.glushko.commands.impl;

import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptyCommand extends AbstractCommand {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug(MessageManager.getMessage(MESSAGE_NULL_PAGE));

        String page = ConfigurationManager.getProperty(PATH_PAGE_INDEX);
        request.getSession().setAttribute(PARAM_NAME_NULL_PAGE, MessageManager.getMessage(MESSAGE_NULL_PAGE));

        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
    }
}
