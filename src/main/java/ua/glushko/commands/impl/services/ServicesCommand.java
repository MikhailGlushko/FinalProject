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

import static ua.glushko.authentification.Authentification.R;

public class ServicesCommand extends AbstractCommand {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            storeRepairServicesListToSession(request);
        }catch (TransactionException | PersistException e) {
           LOGGER.error(e);
        }
        page = ConfigurationManager.getProperty(PATH_PAGE_SERVICES);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServicesListToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try{
            HttpSession session = request.getSession();
            List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
            UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
            String command = request.getParameter(PARAM_NAME_COMMAND);
            int access = Authentification.checkAccess(grantList,userRole,command);
            RepairServicesService service = RepairServicesService.getService();
            Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_NAME_PAGE);
            Integer pageNumber;
            if(parameter==null || parameter.isEmpty())
                pageNumber = 1;
            else
                pageNumber = Integer.valueOf(request.getParameter(PARAM_NAME_PAGE));
            if ((access & R)== R) {
                List<RepairService> items = service.getRepairServiceList(pageNumber, pagesCount, rowsCount);
                List<String> titles = service.getRepairServiceTitles();
                session.setAttribute(PARAM_NAME_SERVICE_LIST_TITLE, titles);
                session.setAttribute(PARAM_NAME_SERVICE_LIST, items);
                session.setAttribute(PARAM_NAME_PAGES_COUNT, pagesCount);
                session.setAttribute(PARAM_NAME_ROWS_COUNT, rowsCount);
                session.setAttribute(PARAM_NAME_PAGE, pageNumber);
            }
        } catch (NullPointerException e){
            LOGGER.error(e);
        }
    }
}