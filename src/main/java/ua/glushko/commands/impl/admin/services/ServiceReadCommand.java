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
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.utils.Authentication.U;

/**
 * Admin Service Management Command, which prepare detail data to show in form
 * @author Mikhail Glushko
 * @version 1.0
 * @see RepairService
 * @see RepairServicesService
 */
public class ServiceReadCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        RepairServicesService repairService = RepairServicesService.getService();
        try {
            if(isUserHasRightToUpdate(request)){
                readService(request, repairService);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void readService(HttpServletRequest request, RepairServicesService repairService) throws ParameterException, SQLException {
        int id = getServiceId(request);
        RepairService item = repairService.getRepairServiceById(id);
        List<String> titles = repairService.getRepairServiceTitles();
        storeServiceData(request, item, titles);
    }

    private void storeServiceData(HttpServletRequest request, RepairService item, List<String> titles) {
        request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST_TITLE, titles);
        request.setAttribute(ServicesCommandHelper.PARAM_SERVICE, item);
    }

    private int getServiceId(HttpServletRequest request) throws ParameterException {
        if (Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)))
            throw new ParameterException("service.id.not.present");
        return Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID));
    }

    private boolean isUserHasRightToUpdate(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & U)==U;

    }
}