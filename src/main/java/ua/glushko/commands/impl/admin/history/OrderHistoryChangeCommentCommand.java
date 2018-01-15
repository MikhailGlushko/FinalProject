package ua.glushko.commands.impl.admin.history;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.orders.OrdersCommandHelper;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersHistoryService;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;

import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS_READ;
import static ua.glushko.commands.impl.admin.history.OrderHistoryCommandHelper.PARAM_NAME_ORDER_HISTORY_MEMO;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_ID;
import static ua.glushko.commands.impl.admin.users.UsersCommandHelper.PARAM_NAME_USER_ID;

public class OrderHistoryChangeCommentCommand extends Command {
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
        Integer orderId = null;
        Integer userId = null;
        try {
            orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
            userId = Integer.valueOf(request.getSession().getAttribute(Authentification.PARAM_NAME_ID).toString());
        } catch (NumberFormatException e){
            LOGGER.error(e);
        }
        String comment = request.getParameter(PARAM_NAME_ORDER_HISTORY_MEMO);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setAction(Action.ADD_COMMENT.name());
        orderHistory.setNewValue(comment);
        orderHistory.setUserId(userId);
        orderHistory.setOrderId(orderId);
        orderHistory.setDecription(comment);
        ordersHistoryService.updateOrderHistoty(orderHistory);
    }
}
