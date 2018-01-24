package ua.glushko.commands.impl.admin.orders;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.*;
import static ua.glushko.services.utils.Authentication.R;

public class OrdersListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServicesListToSession(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_ORDERS);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServicesListToSession(HttpServletRequest request) throws SQLException, TransactionException, ParameterException {
        HttpSession session = request.getSession();
        int access = Authentication.checkAccess(request);
        OrdersService ordersService = OrdersService.getService();
        int pagesCount = 0;
        int rowsCount = 0;
        int pageNumber = 1;
        int userId = 0;
        try {
            pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_PAGE);
            if (!(parameter == null || parameter.isEmpty() || parameter.equals("null")))
                pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
            userId = Integer.valueOf(session.getAttribute(Authentication.PARAM_ID).toString());
        } catch (NumberFormatException e) {
            LOGGER.debug(e);
        }
        List<Order> items = null;
        List<Order> itemsS = null;
        if ((access & R) == R) {
            items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount);
            itemsS = ordersService.getOrderListByStatus(pageNumber, pagesCount, rowsCount);
        } else if ((access & Authentication.r) == Authentication.r) {
            items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount, userId);
        }
        List<String> titles = ordersService.getOrderTitles();
        Integer countNew = ordersService.countNew();
        int count = ordersService.count(userId);
        count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
        request.setAttribute(PARAM_ORDERS_LIST_TITLE, titles);
        request.setAttribute(PARAM_ORDERS_LIST, items);
        request.setAttribute(PARAM_ORDERS_SLIST, itemsS);
        request.setAttribute(PARAM_LAST_PAGE, count);
        request.setAttribute(PARAM_ORDERS_COUNT_NEW,countNew);
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, access);
        LOGGER.debug("Orders list were getted and try to show");
    }
}