package ua.glushko.commands.impl.services;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_SERVICES;

public class ServiceSaveCommand extends AbstractCommand {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page;
        try {
            storeUsersDataToDatabase(request);
        }catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        page = "/do?command="+COMMAND_NAME_SERVICES;
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);

    }

    private void storeUsersDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException{
        try{
            String button = request.getParameter("button");
            HttpSession session = request.getSession();
            List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
            UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
            String command = request.getParameter(PARAM_NAME_COMMAND);
            int access = Authentification.checkAccess(grantList,userRole,command);
            if("save".equals(button)) {
                Integer serviceId = Integer.valueOf(request.getParameter(PARAM_NAME_SERVICE_ID));
                String ame = request.getParameter(PARAM_NAME_SERVICE_NAME);
                String nameRu = request.getParameter(PARAM_NAME_SERVICE_NAME_RU);
                Integer parent = Integer.valueOf(request.getParameter(PARAM_NAME_SERVICE_NAME_RU));
                RepairServicesService service = RepairServicesService.getService();
                // get user data drom database
                RepairService item = service.getRepairServiceById(serviceId);
                item.setName(ame);
                item.setNameRu(nameRu);
                item.setParent(parent);
                if ((access & U) == U) {
                    service.updateRepairService(item);
                }
                session.setAttribute(PARAM_NAME_SERVICE, item);
                request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_SERVICES);
            } else {
                Integer serviceId = Integer.valueOf(request.getParameter(PARAM_NAME_SERVICE_ID));
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
