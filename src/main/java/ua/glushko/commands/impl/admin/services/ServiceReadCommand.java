package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.utils.Authentication.U;

/**
 * Display information about the type of service with the ability to edit or delete
 */
public class ServiceReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & U) == U) {
                if (Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)))
                    throw new ParameterException("service.id.not.present");
                int id = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
                RepairServicesService repairService = RepairServicesService.getService();
                RepairService item = repairService.getRepairServiceById(id);
                List<String> titles = repairService.getRepairServiceTitles();
                request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST_TITLE, titles);
                request.setAttribute(ServicesCommandHelper.PARAM_SERVICE, item);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES_DETAIL);
        return new CommandRouter(request, response, page);
    }
}