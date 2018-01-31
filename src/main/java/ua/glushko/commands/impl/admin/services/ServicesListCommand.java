package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.RepairService;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static ua.glushko.commands.utils.Authentication.R;

/**
 * Admin Service Management Command, which prepare list of repairServices to show
 * @author Mikhail Glushko
 * @version 1.0
 * @see RepairService
 * @see RepairServicesService
 */
public class ServicesListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        RepairServicesService service = RepairServicesService.getService();
        try {
            if(isUserHasRightToRead(request)){
                listService(request, service);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES);
        return new CommandRouter(request, response, page);
    }

    private void listService(HttpServletRequest request, RepairServicesService service) throws SQLException, TransactionException, ParameterException {
        Integer pageNumber = getPageNumber(request);
        List<RepairService> items = service.getRepairServiceList(pageNumber);
        List<String> titles = service.getRepairServiceTitles();
        int count = service.count();
        storeServiceData(request, pageNumber, items, titles, count);
        LOGGER.debug("RepairServices list were got and try to show");
    }

    private void storeServiceData(HttpServletRequest request, Integer pageNumber, List<RepairService> items, List<String> titles, int count) throws ParameterException {
        Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
        request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST_TITLE, titles);
        request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST, items);
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, Authentication.checkAccess(request));
        request.setAttribute(PARAM_LAST_PAGE, count);
    }

    private Integer getPageNumber(HttpServletRequest request) {
        Integer pageNumber;
        String parameter = request.getParameter(PARAM_PAGE);
        if (parameter == null || parameter.isEmpty() || parameter.equals("null"))
            pageNumber = 1;
        else
            pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
        return pageNumber;
    }

    private boolean isUserHasRightToRead(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & R)==R;

    }
}