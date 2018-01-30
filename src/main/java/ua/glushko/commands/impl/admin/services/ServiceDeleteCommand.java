package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.D;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/**
 * Admin Service Management Command, which receives data from the form and delete record from database
 * @author Mikhail Glushko
 * @version 1.0
 * @see RepairService
 * @see RepairServicesService
 */
public class ServiceDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & D) == D) {
                if (Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)))
                    throw new ParameterException("service.not.present");
                Integer Id = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
                RepairService repairService;
                RepairServicesService service = RepairServicesService.getService();
                LOGGER.debug("deleting service " + Id);
                repairService = service.getRepairServiceById(Id);
                service.deleteRepairService(Id);
                LOGGER.debug("service " + repairService + " was deleted");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        request.setAttribute(PARAM_COMMAND, COMMAND_SERVICES);
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }
}
