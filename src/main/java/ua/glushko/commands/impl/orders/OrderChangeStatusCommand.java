package ua.glushko.commands.impl.orders;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.utils.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_READ;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.*;
import static ua.glushko.services.utils.Authentication.PARAM_NAME_NAME;
import static ua.glushko.services.utils.Authentication.U;
import static ua.glushko.services.utils.Authentication.u;

/** Update data after editing */
public class OrderChangeStatusCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeOrderToDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_ORDERS +"&page=" + request.getParameter(PARAM_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeOrderToDatabase(HttpServletRequest request) {
        Integer orderId = null;
        try {
            int access = Authentication.checkAccess(request);
            orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
            OrdersService ordersService= OrdersService.getService();
            Order order = ordersService.getOrderById(orderId);

            String action = request.getParameter(PARAM_ORDER_FORM_ACTION);
            String actionMemo = request.getParameter(PARAM_ORDER_MEMO_CHANGE);
            String userName = (String) request.getSession().getAttribute(PARAM_NAME_NAME);
            String newStatus = request.getParameter(PARAM_ORDER_STATUS_CHANGE);
            String newEmployeeId = request.getParameter(PARAM_ORDER_EMPLOYEE_ID_CHANGE);
            switch (action){
                case "approve":
                    order.setStatus(OrderStatus.valueOf(newStatus));
                    if(order.getStatus()==OrderStatus.COMPLETE && order.getManagerId()!=0)
                        order.setEmployeeId(order.getManagerId());
                    if(Objects.nonNull(newEmployeeId))
                        order.setEmployeeId(Integer.valueOf(newEmployeeId));
                    String oldMemo = order.getMemo();
                    if(Objects.isNull(oldMemo))
                        oldMemo="";
                    order.setMemo(new StringBuilder(oldMemo).append("\n")
                            .append(new Date(System.currentTimeMillis())).append(" ")
                            .append(userName).append(" ")
                            .append(action).append(" ")
                            .append(order.getStatus())
                            .toString());
                    break;
                case "reject":
                    order.setStatus(OrderStatus.REJECT);
                    oldMemo = order.getMemo();
                    if(Objects.isNull(oldMemo))
                        oldMemo="";
                    order.setMemo(new StringBuilder(oldMemo).append("\n")
                            .append(new Date(System.currentTimeMillis())).append(" ")
                            .append(userName).append(" ")
                            .append(action).append(" ")
                            .append(actionMemo).toString());
                    order.setEmployeeId(order.getUserId());
                    break;
            }
            if ((access & U) == U || (access & u) == u) {
                ordersService.updateOrder(order);
            }
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, order);
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
            LOGGER.debug("order " + orderId+" was updated");
        } catch (Exception e) {
            LOGGER.debug("order " + orderId+" was not update");
            LOGGER.error(e);
        }
    }
}
