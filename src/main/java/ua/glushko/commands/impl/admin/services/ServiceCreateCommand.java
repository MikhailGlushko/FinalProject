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

import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.C;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/**
 * Admin Service Management Command, which receives data from the form and creates a new record
 * @author Mikhail Glushko
 * @version 1.0
 * @see RepairService
 * @see RepairServicesService
 */
public class ServiceCreateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & C) == C) {
                String name = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME);
                if (Objects.isNull(name))
                    throw new ParameterException("service.name.not.present");
                String nameRu = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME_RU);
                if (Objects.isNull(nameRu))
                    throw new ParameterException("service.name.ru.not.present");
                if(Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_PARENT)))
                    throw new ParameterException("service.parent.not.present");
                Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_PARENT));
                RepairServicesService service = RepairServicesService.getService();
                RepairService repairService = new RepairService();
                repairService.setName(name);
                repairService.setNameRu(nameRu);
                repairService.setParent(parent);
                LOGGER.debug("creating new service " + repairService);
                service.updateRepairService(repairService);
                LOGGER.debug("new service " + repairService + " was created");
                int count = service.count();
                Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
                request.setAttribute(PARAM_LAST_PAGE, count);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_LAST_PAGE);
        return new CommandRouter(request, response, page);
    }
}
