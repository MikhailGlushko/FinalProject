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
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/**
 * Update data data after editing
 */
public class ServiceUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & U) == U) {
                if(Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)))
                    throw new ParameterException("service.is.not.present");
                Integer serviceId = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
                String name = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME);
                String nameRu = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME_RU);
                Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_PARENT));
                RepairServicesService service = RepairServicesService.getService();
                // get user data from database
                RepairService item = service.getRepairServiceById(serviceId);
                item.setName(name);
                item.setNameRu(nameRu);
                item.setParent(parent);
                service.updateRepairService(item);
                request.setAttribute(ServicesCommandHelper.PARAM_SERVICE, item);
                request.setAttribute(PARAM_COMMAND, COMMAND_SERVICES);
                LOGGER.debug("service " + serviceId + " was updated");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }
}
