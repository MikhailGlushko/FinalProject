package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Date;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.authentification.Authentification.u;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS;

/** Обновление данных о пользователе после редактирования */
public class OrderUpdateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeOrderToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_ORDERS+"&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeOrderToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer orderId = null;
        try {
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));

            String  orderDescriptionShort = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_DESC_SHORT);
            String  orderDescriptionDetail = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_DESC_DETAIL);
            Integer  orderRepairService = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_SERVICE));
            String  orderCity = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_CITY);
            String  orderStreet = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_STREET);
            String  orderOrderDate = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_DATE);
            String  orderExpectedDate = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_EXPECTED_DATE);
            String  orderAppliance = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_APPL);
            Integer  orderUserId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_USER_ID));
            String  orderMemo = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_APPL);
            Integer orderEmployeeId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_EMPLOYEE_ID));
            String  orderStatus = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_STATUS);

            OrdersService ordersService= OrdersService.getService();
            // get user data from database
            Order item = ordersService.getOrderById(orderId);
            item.setDescriptionShort(orderDescriptionShort);
            item.setDescriptionDetail(orderDescriptionDetail);
            item.setRepairService(orderRepairService);
            item.setCity(orderCity);
            item.setStreet(orderStreet);
            //item.setOrderDate();
            //item.setExpectedDate();
            item.setAppliance(orderAppliance);
            item.setUserId(orderUserId);
            item.setMemo(orderMemo);
            item.setStatus(orderStatus);
            item.setEmployeeId(orderEmployeeId);
            if ((access & U) == U || (access & u) == u) {
                ordersService.updateOrder(item);
            }
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, item);
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_ORDERS);
            LOGGER.debug("order " + orderId+" was updated");
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("order " + orderId+" was not update");
            LOGGER.error(e);
        }
    }
}
