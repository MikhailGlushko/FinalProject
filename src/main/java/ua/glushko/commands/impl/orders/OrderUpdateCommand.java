package ua.glushko.commands.impl.orders;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.Order;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS_READ;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.PARAM_ORDER_ID;
import static ua.glushko.services.utils.Authentication.U;
import static ua.glushko.services.utils.Authentication.u;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/** Update data after editing */
public class OrderUpdateCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeOrderToDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_ORDERS +"&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeOrderToDatabase(HttpServletRequest request) {
        Integer orderId = null;
        try {
            int access = Authentication.checkAccess(request);
            orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));

            String  orderDescriptionShort = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_SHORT);
            String  orderDescriptionDetail = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_DETAIL);
            Integer  orderRepairService = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_SERVICE));
            String  orderCity = request.getParameter(OrdersCommandHelper.PARAM_ORDER_CITY);
            String  orderStreet = request.getParameter(OrdersCommandHelper.PARAM_ORDER_STREET);
            String  orderExpectedDate = request.getParameter(OrdersCommandHelper.PARAM_ORDER_EXPECTED_DATE);
            String  orderAppliance = request.getParameter(OrdersCommandHelper.PARAM_ORDER_APPL);
            Integer orderUserId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_USER_ID));
            String  orderMemo = request.getParameter(OrdersCommandHelper.PARAM_ORDER_MEMO);
            Integer orderEmployeeId = null;
            if(Objects.nonNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EMPLOYEE_ID)))
                orderEmployeeId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EMPLOYEE_ID));
            String  orderStatus = request.getParameter(OrdersCommandHelper.PARAM_ORDER_STATUS);
            Double orderPrice = null;
            if(Objects.nonNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_PRICE)))
                orderPrice = Double.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_PRICE));

            OrdersService ordersService= OrdersService.getService();
            // get user data from database
            Order item = ordersService.getOrderById(orderId);
            //validateAccessToUdate(Authentication.getCurrentUserId(request.getSession()));
            item.setDescriptionShort(orderDescriptionShort);
            item.setDescriptionDetail(orderDescriptionDetail);
            item.setRepairService(orderRepairService);
            item.setCity(orderCity);
            item.setStreet(orderStreet);
            item.setAppliance(orderAppliance);
            item.setUserId(orderUserId);
            item.setMemo(orderMemo);
            item.setStatus(orderStatus);
            if(Objects.nonNull(orderEmployeeId))
                item.setEmployeeId(orderEmployeeId);
            if(Objects.nonNull(orderPrice))
                item.setPrice(orderPrice);
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = format.parse(orderExpectedDate);
            } catch (ParseException e) {
            }
            item.setExpectedDate(date);
            if ((access & U) == U || (access & u) == u) {
                ordersService.updateOrder(item);
            }
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, item);
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
            LOGGER.debug("order " + orderId+" was updated");
        } catch (Exception e) {
            LOGGER.debug("order " + orderId+" was not update");
            LOGGER.error(e);
        }
    }
}
