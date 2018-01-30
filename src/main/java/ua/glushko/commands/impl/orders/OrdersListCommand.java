package ua.glushko.commands.impl.orders;

import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.OrderStats;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;
import static ua.glushko.commands.utils.Authentication.R;

/**
 * Admin Order Management Command, which prepare list of orders to show
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrdersListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int access = Authentication.checkAccess(request);
            if(Objects.isNull(request.getSession().getAttribute(Authentication.PARAM_ID)))
                throw new ParameterException("user.didn't.login");
            int userId = Integer.valueOf(request.getSession().getAttribute(Authentication.PARAM_ID).toString());
            int rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            int pageNumber = 1;
            String parameter = request.getParameter(PARAM_PAGE);
            if (!(parameter == null || parameter.isEmpty() || parameter.equals("null")))
                pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
            OrdersService ordersService = OrdersService.getService();
            List<Order> items = null;
            if ((access & R) == R) {
                items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount);
            } else if ((access & Authentication.r) == Authentication.r) {
                items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount, userId);
            }
            List<String> titles = ordersService.getOrderTitles();
            Map<OrderStatus, Map<OrderStats, Integer>> stats = ordersService.getStats(Authentication.getCurrentUserId(request.getSession()));
            if (Objects.nonNull(stats)) {
                Map<OrderStats, Integer> orderStatsNew = stats.get(OrderStatus.NEW);
                Integer countNew = orderStatsNew.get(OrderStats.STATUS);
                request.setAttribute(PARAM_ORDERS_COUNT_NEW, countNew);
            }
            int count = ordersService.count(userId);
            count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
            request.setAttribute(PARAM_ORDERS_LIST_TITLE, titles);
            request.setAttribute(PARAM_ORDERS_LIST, items);
            request.setAttribute(PARAM_LAST_PAGE, count);
            request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
            request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
            request.setAttribute(PARAM_PAGE, pageNumber);
            request.setAttribute(PARAM_ACCESS, access);
            LOGGER.debug("Orders list were got and try to show");
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_ORDERS);
        return new CommandRouter(request, response, page);
    }
}