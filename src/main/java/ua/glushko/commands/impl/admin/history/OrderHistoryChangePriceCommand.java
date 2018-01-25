package ua.glushko.commands.impl.admin.history;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.orders.OrdersCommandHelper;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.OrdersHistoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_READ;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_MEMO;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_PRICE;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.PARAM_ORDER_ID;

public class OrderHistoryChangePriceCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeHistoryDataToDatabase(request);
        } catch (TransactionException | DatabaseException | ParseException | ParameterException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_ORDERS_READ + "&order_id=" + request.getParameter(PARAM_ORDER_ID);
        return new CommandRouter(request, response, page);
    }

    private void storeHistoryDataToDatabase(HttpServletRequest request) throws TransactionException, ParseException, ParameterException, DatabaseException {
        String parameter = request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID);
        if(Objects.isNull(parameter))
            throw new ParameterException("order.id.is.null");
        Object attribute = request.getSession().getAttribute(Authentication.PARAM_ID);
        if(Objects.isNull(attribute))
            throw new ParameterException("id.is.null");
        OrdersHistoryService ordersHistoryService = OrdersHistoryService.getService();
        Integer orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
        Integer userId = Integer.valueOf(request.getSession().getAttribute(Authentication.PARAM_ID).toString());
        String comment = request.getParameter(PARAM_NAME_ORDER_HISTORY_MEMO);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setAction(Action.CHANGE_PRICE.name());
        orderHistory.setDescription(comment);
        String requestParameter = request.getParameter(PARAM_NAME_ORDER_HISTORY_PRICE);
        orderHistory.setNewValue(requestParameter);
        orderHistory.setUserId(userId);
        orderHistory.setOrderId(orderId);
        ordersHistoryService.updateOrderHistory(orderHistory);
    }
}
