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

import static ua.glushko.authentification.Authentification.C;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_SERVICES;

/** Создание нового пользователя */
public class ServiceCreateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeUserDataToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_SERVICES + "&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeUserDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        String userLogin = null;
        RepairService repairService=null;
        try {
            int access = Authentification.checkAccess(request);

            String name = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME);
            String nameRu = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME_RU);
            Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_PARENT));

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
            }
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("new service "+repairService+" did not create");
            LOGGER.error(e);
        }
    }
}
