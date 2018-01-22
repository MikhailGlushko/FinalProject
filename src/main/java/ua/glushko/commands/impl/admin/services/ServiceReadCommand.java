package ua.glushko.commands.impl.admin.services;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.DaoException;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.authentification.Authentication.U;

/**  Display information about the type of service with the ability to edit or delete */
public class ServiceReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServiceDetailToSession(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServiceDetailToSession(HttpServletRequest request) throws DaoException, TransactionException, ParameterException {
        int access = Authentication.checkAccess(request);
        int id=0;
        try {
            id = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
        }catch (NumberFormatException e){
            LOGGER.error(e);
        }
        RepairServicesService repairService = RepairServicesService.getService();
        if ((access & U) == U) {
            RepairService item = repairService.getRepairServiceById(id);
            List<String> titles = repairService.getRepairServiceTitles();
            request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST_TITLE, titles);
            request.setAttribute(ServicesCommandHelper.PARAM_SERVICE, item);
        }
    }
}