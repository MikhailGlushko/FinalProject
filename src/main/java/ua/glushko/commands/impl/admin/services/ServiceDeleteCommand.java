package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.D;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/** Delete entity from database */
public class ServiceDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            deleteUserDataFromDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void deleteUserDataFromDatabase(HttpServletRequest request) throws TransactionException, DatabaseException {
        Integer Id;
        RepairService repairService;
        try {
            int access = Authentication.checkAccess(request);
            RepairServicesService service = RepairServicesService.getService();
            Id = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting service " + Id);
                // update user data into database
                repairService = service.getRepairServiceById(Id);
                service.deleteRepairService(Id);
                LOGGER.debug("service "+repairService+" was deleted");
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_SERVICES);
        } catch (ParameterException e) {
            LOGGER.debug("service did not delete");
            LOGGER.error(e);
        }
    }
}
