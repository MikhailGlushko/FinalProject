package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.R;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_ORDERS_LIST;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_ORDERS_LIST_TITLE;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PATH_PAGE_ORDERS;

public class OrdersListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServicesListToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        } catch (ParameterException e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_ORDERS);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServicesListToSession(HttpServletRequest request) throws PersistException, TransactionException, ParameterException {
        HttpSession session = request.getSession();
        int access = Authentification.checkAccess(request);
        OrdersService ordersService = OrdersService.getService();
        Integer pagesCount = null;
        Integer rowsCount = null;
        Integer pageNumber = null;
        Integer userId = null;
        try {
            pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_PAGE);
            if (parameter == null || parameter.isEmpty() || parameter.equals("null"))
                pageNumber = 1;
            else
                pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
            userId = Integer.valueOf(session.getAttribute(Authentification.PARAM_ID).toString());
        } catch (NumberFormatException e) {
            LOGGER.debug(e);
        }
        if ((access & R) == R) {
            List<Order> items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount);
            List<String> titles = ordersService.getOrderTitles();
            int count = ordersService.count();
            count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
            request.setAttribute(PARAM_ORDERS_LIST_TITLE, titles);
            request.setAttribute(PARAM_ORDERS_LIST, items);
            request.setAttribute(PARAM_LAST_PAGE, count);
        } else if ((access & Authentification.r) == Authentification.r) {
            List<Order> items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount, userId);
            List<String> titles = ordersService.getOrderTitles();
            int count = ordersService.count(userId);
            count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
            request.setAttribute(PARAM_ORDERS_LIST_TITLE, titles);
            request.setAttribute(PARAM_ORDERS_LIST, items);
            request.setAttribute(PARAM_LAST_PAGE, count);
        }
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, access);
        LOGGER.debug("Orders list were getted and try to show");
    }
}