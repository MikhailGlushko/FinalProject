package ua.glushko.commands.impl.services;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
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

/**
 * Отображение списка пользователей
 */
public class ServiceDetailCommand extends AbstractCommand {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            storeRepairServiceDetailToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_SERVICES_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServiceDetailToSession(HttpServletRequest request) throws PersistException, TransactionException {
        HttpSession session = request.getSession();
        List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
        UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
        String command = request.getParameter(PARAM_NAME_COMMAND);
        int access = Authentification.checkAccess(grantList, userRole, command);
        Integer id = Integer.valueOf(request.getParameter(PARAM_NAME_SERVICE_ID));
        RepairServicesService repairService = RepairServicesService.getService();
        if ((access & U) == U) {
            RepairService item = repairService.getRepairServiceById(id);
            List<String> titles = repairService.getRepairServiceTitles();
            session.setAttribute(PARAM_NAME_SERVICE_LIST_TITLE, titles);
            session.setAttribute(PARAM_NAME_SERVICE, item);
        }
    }
}