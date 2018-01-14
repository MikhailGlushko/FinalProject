package ua.glushko.commands.impl.admin.services;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.authentification.Authentification.D;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_SERVICES;

/** Удаление существующего пользователя */
public class ServiceDeleteCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            deleteUserDataFromDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_SERVICES + "&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void deleteUserDataFromDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer Id = null;
        RepairService repairService = null;
        try {
            int access = Authentification.checkAccess(request);
            RepairServicesService service = RepairServicesService.getService();
            Id = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting service " + Id);
                // update user data into database
                repairService = service.getRepairServiceById(Id);
                service.deleteRepairService(Id);
                LOGGER.debug("service "+repairService+" was deleted");
            }
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_SERVICES);
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("service "+repairService+" did not delete");
            LOGGER.error(e);
        }
    }
}
