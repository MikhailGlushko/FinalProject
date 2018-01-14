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
import javax.servlet.http.HttpSession;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_SERVICES;

public class ServiceSaveCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeUsersDataToDatabase(request);
        }catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_SERVICES;
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);

    }

    private void storeUsersDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException{
        try{
            String button = request.getParameter("button");
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            if("save".equals(button)) {
                Integer serviceId = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_ID));
                String name = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME);
                String nameRu = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME_RU);
                Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_PARENT));
                RepairServicesService service = RepairServicesService.getService();
                // get user data from database
                RepairService item = service.getRepairServiceById(serviceId);
                item.setName(name);
                item.setNameRu(nameRu);
                item.setParent(parent);
                if ((access & U) == U) {
                    service.updateRepairService(item);
                }
                session.setAttribute(ServicesCommandHelper.PARAM_NAME_SERVICE, item);
                request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_SERVICES);
            } else {
                Integer serviceId = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_ID));
                RepairServicesService service = RepairServicesService.getService();
                if ((access & U) == U) {
                    service.deleteRepairService(serviceId);
                }
                request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_SERVICES);
            }
        } catch (NullPointerException | NumberFormatException e){
            LOGGER.error(e);
        }
    }
}
