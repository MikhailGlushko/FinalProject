package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Admin RepairService Management Command that redirect to the form of adding a new entity by pressing the "+" button on the form with the list
 * @version 1.0
 * @author Mikhail Glushko
 */
public class ServiceAddCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICE_ADD);
        return new CommandRouter(request, response, page);
    }
}