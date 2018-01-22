package ua.glushko.commands.impl.admin.services;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;

import static ua.glushko.services.utils.Authentication.C;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/** Create New entity */
public class ServiceCreateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeUserDataToDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_LAST_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeUserDataToDatabase(HttpServletRequest request) throws SQLException, TransactionException {
        RepairService repairService=null;
        try {
            int access = Authentication.checkAccess(request);

            String name = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME);
            String nameRu = request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME_RU);
            Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_PARENT));

            RepairServicesService service = RepairServicesService.getService();
            repairService = new RepairService();
            repairService.setName(name);
            repairService.setNameRu(nameRu);
            repairService.setParent(parent);
            if ((access & C) == C) {
                LOGGER.debug("creating new service "+repairService);
                // update user data into database
                service.updateRepairService(repairService);
                LOGGER.debug("new service "+repairService+" was created");
                int count = service.count();
                Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                request.setAttribute(PARAM_LAST_PAGE,count);
            }
        } catch (ParameterException e) {
            LOGGER.debug("new service "+repairService+" did not create");
            LOGGER.error(e);
        }
    }
}
