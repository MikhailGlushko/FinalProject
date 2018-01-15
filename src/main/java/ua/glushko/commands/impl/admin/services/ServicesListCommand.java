package ua.glushko.commands.impl.admin.services;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.R;

public class ServicesListCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServicesListToSession(request);
        }catch (TransactionException | PersistException e) {
           LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServicesListToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try{
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
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
                int count = service.count();
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                session.setAttribute(ServicesCommandHelper.PARAM_NAME_SERVICE_LIST_TITLE, titles);
                session.setAttribute(ServicesCommandHelper.PARAM_NAME_SERVICE_LIST, items);
                session.setAttribute(PARAM_NAME_PAGES_COUNT, pagesCount);
                session.setAttribute(PARAM_NAME_ROWS_COUNT, rowsCount);
                session.setAttribute(PARAM_NAME_PAGE, pageNumber);
                session.setAttribute(PARAM_NAME_ACCESS,access);
                session.setAttribute(PARAM_NAME_LAST_PAGE,count);
            }
        } catch (NullPointerException | NumberFormatException e){
            LOGGER.error(e);
        }
    }
}