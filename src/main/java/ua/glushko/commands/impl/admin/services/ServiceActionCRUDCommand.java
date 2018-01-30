package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.*;

/**
 * Admin RepairService Management Command that receives data from a form and analyzes what operation it must perform on the data
 * @version 1.0
 * @author Mikhail Glushko
 * @see ServiceCreateCommand
 * @see ServiceCreateCommand
 * @see ServiceDeleteCommand
 */
public class ServiceActionCRUDCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String action = request.getParameter("action");
        request.setAttribute(PARAM_PAGE, request.getParameter(PARAM_PAGE));
        switch (action) {
            case "save":
                page = PARAM_SERVLET_PATH+"?command=" + COMMAND_SERVICES_UPDATE+"&page="+request.getParameter(PARAM_PAGE);
                break;
            case "add":
                page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES_CREATE+"&page="+request.getParameter(PARAM_LAST_PAGE);
                break;
            case "delete":
                page =  PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES_DELETE;
                break;
        }
        return new CommandRouter(request, response, page);
    }
}
