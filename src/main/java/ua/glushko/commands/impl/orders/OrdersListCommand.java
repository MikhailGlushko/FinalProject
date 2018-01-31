package ua.glushko.commands.impl.orders;

import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;
import static ua.glushko.commands.utils.Authentication.R;
import static ua.glushko.commands.utils.Authentication.r;

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
        OrdersService ordersService = OrdersService.getService();
        try {
            prepareListData(request, ordersService);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_ORDERS);
        return new CommandRouter(request, response, page);
    }

    private void prepareListData(HttpServletRequest request, OrdersService ordersService) throws ParameterException, SQLException, TransactionException {
        int userId = getUserId(request);
        int pageNumber = getPageNumber(request);
        List<Order> items = prepareOrderList(request, ordersService, userId, pageNumber);
        List<String> titles = ordersService.getOrderTitles();
        prepareStats(request, ordersService);
        int count = ordersService.count(userId);
        storeOrderData(request, pageNumber, items, titles, count);
        LOGGER.debug("Orders list were got and try to show");
    }

    private void prepareStats(HttpServletRequest request, OrdersService ordersService) throws SQLException {
        Map<OrderStatus, Map<OrderStats, Integer>> stats = ordersService.getStats(Authentication.getCurrentUserId(request.getSession()));
        if (Objects.nonNull(stats)) {
            Map<OrderStats, Integer> orderStatsNew = stats.get(OrderStatus.NEW);
            Integer countNew = null;
            if(Objects.nonNull(orderStatsNew))
            countNew = orderStatsNew.get(OrderStats.STATUS);
            if(Objects.nonNull(countNew))
            request.setAttribute(PARAM_ORDERS_COUNT_NEW, countNew);
        }
    }

    private List<Order> prepareOrderList(HttpServletRequest request, OrdersService ordersService, int userId, int pageNumber) throws ParameterException, SQLException, TransactionException {
        List<Order> items = null;
        if(isUserHasRigthToReadAll(request)){
            items = ordersService.getOrderList(pageNumber);
        } else if (isUserHasRigthToReadOwnOnly(request)) {
            items = ordersService.getOrderList(pageNumber,userId);
        }
        return items;
    }

    private boolean isUserHasRigthToReadAll(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & R)==R;
    }

    private boolean isUserHasRigthToReadOwnOnly(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & r)==r;
    }

    private int getPageNumber(HttpServletRequest request) {
        int pageNumber = 1;
        String parameter = request.getParameter(PARAM_PAGE);
        if (!(parameter == null || parameter.isEmpty() || parameter.equals("null")))
            pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
        return pageNumber;
    }

    private int getUserId(HttpServletRequest request) throws ParameterException {
        if(Objects.isNull(request.getSession().getAttribute(Authentication.PARAM_ID)))
            throw new ParameterException("user.didn't.login");
        return Integer.valueOf(request.getSession().getAttribute(Authentication.PARAM_ID).toString());
    }

    private void storeOrderData(HttpServletRequest request, int pageNumber, List<Order> items, List<String> titles, int count) throws ParameterException {
        int rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
        request.setAttribute(PARAM_ORDERS_LIST_TITLE, titles);
        request.setAttribute(PARAM_ORDERS_LIST, items);
        request.setAttribute(PARAM_LAST_PAGE, count);
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, Authentication.checkAccess(request));
    }
}