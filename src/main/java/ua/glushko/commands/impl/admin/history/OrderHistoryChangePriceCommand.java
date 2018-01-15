package ua.glushko.commands.impl.admin.history;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.orders.OrdersCommandHelper;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersHistoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;

import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS_READ;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_EMPLOYEE_ID;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_MEMO;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_PRICE;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_ID;

public class OrderHistoryChangePriceCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeHistoryDataToDatabase(request);
        } catch (TransactionException | PersistException | ParseException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_ORDERS_READ + "&order_id=" + request.getParameter(PARAM_NAME_ORDERS_ID);
        return new CommandRouter(request, response, page);
    }

    private void storeHistoryDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException, ParseException {
        OrdersHistoryService ordersHistoryService = OrdersHistoryService.getService();
        Integer orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
        Integer userId  = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_USER_ID));
        String comment = request.getParameter(PARAM_NAME_ORDER_HISTORY_MEMO);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setAction(Action.CHANGE_PRICE.name());
        orderHistory.setDecription(comment);
        String requestParameter = request.getParameter(PARAM_NAME_ORDER_HISTORY_PRICE);
        orderHistory.setNewValue(requestParameter);
        orderHistory.setUserId(userId);
        orderHistory.setOrderId(orderId);
        ordersHistoryService.updateOrderHistoty(orderHistory);
    }
}
