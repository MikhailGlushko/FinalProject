package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ua.glushko.commands.utils.Authentication.R;

public class ServicesListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if ((access & R) == R) {
                RepairServicesService service = RepairServicesService.getService();
                Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
                Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                String parameter = request.getParameter(PARAM_PAGE);
                Integer pageNumber;
                if (parameter == null || parameter.isEmpty() || parameter.equals("null"))
                    pageNumber = 1;
                else
                    pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
                List<RepairService> items = service.getRepairServiceList(pageNumber, pagesCount, rowsCount);
                List<String> titles = service.getRepairServiceTitles();
                int count = service.count();
                count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
                request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST_TITLE, titles);
                request.setAttribute(ServicesCommandHelper.PARAM_SERVICE_LIST, items);
                request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
                request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
                request.setAttribute(PARAM_PAGE, pageNumber);
                request.setAttribute(PARAM_ACCESS, access);
                request.setAttribute(PARAM_LAST_PAGE, count);
                LOGGER.debug("RepairServices list were got and try to show");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICES);
        return new CommandRouter(request, response, page);
    }
}