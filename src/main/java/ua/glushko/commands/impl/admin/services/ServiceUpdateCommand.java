package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.U;
import static ua.glushko.commands.CommandFactory.COMMAND_SERVICES;

/**
 * Admin Service Management Command, which receives data from the form and update item in Database
 * @author Mikhail Glushko
 * @version 1.0
 * @see RepairService
 * @see RepairServicesService
 */
public class ServiceUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        RepairServicesService service = RepairServicesService.getService();
        try {
            if(isUserHasRightToUpdate(request)){
                updateService(request, service);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_SERVICES + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void updateService(HttpServletRequest request, RepairServicesService service) throws ParameterException, SQLException, TransactionException {
        RepairService old = getServiceData(request);
        RepairService item = service.getRepairServiceById(old.getId());
        item.setName(old.getName());
        item.setNameRu(old.getNameRu());
        item.setParent(old.getParent());
        service.updateRepairService(item);
        request.setAttribute(ServicesCommandHelper.PARAM_SERVICE, item);
        request.setAttribute(PARAM_COMMAND, COMMAND_SERVICES);
        LOGGER.debug("service " + item.getId() + " was updated");
    }

    private RepairService getServiceData(HttpServletRequest request) throws ParameterException {
        RepairService item = new RepairService();
        if(Objects.isNull(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)))
            throw new ParameterException("service.is.not.present");
        item.setId(Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_ID)));
        item.setName(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME));
        item.setNameRu(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_NAME_RU));
        item.setParent(Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_SERVICE_PARENT)));
        return item;


    }

    private boolean isUserHasRightToUpdate(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & U) ==U;
    }
}
