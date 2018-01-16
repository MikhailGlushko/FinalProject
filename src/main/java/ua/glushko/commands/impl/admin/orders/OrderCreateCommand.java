package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.Order;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ua.glushko.authentification.Authentification.C;
import static ua.glushko.authentification.Authentification.c;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS;

/** Create new order */
public class OrderCreateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeOrderDataToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_ORDERS + "&page=" + request.getSession().getAttribute(PARAM_NAME_LAST_PAGE);
        return new CommandRouter(request, response, page);

    }

    private void storeOrderDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Order order = null;
        try {
            int access = Authentification.checkAccess(request);
            // getting data from form
            String  orderDescriptionShort = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_DESC_SHORT);
            String  orderDescriptionDetail = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_DESC_DETAIL);
            Integer  orderRepairService = null;
            Integer  orderUserId = null;
            try {
                orderRepairService = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_SERVICE));
                orderUserId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_USER_ID));
            }catch (NumberFormatException e){
                LOGGER.error(e);
            }
            String  orderCity = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_CITY);

            String  orderStreet = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_STREET);
            String  orderExpectedDate = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_EXPECTED_DATE);
            String  orderAppliance = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_APPL);
            String  orderMemo = request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_APPL);

            OrdersService ordersService = OrdersService.getService();
            order = new Order();
            order.setDescriptionShort(orderDescriptionShort);
            order.setDescriptionDetail(orderDescriptionDetail);
            order.setRepairService(orderRepairService);
            order.setCity(orderCity);
            order.setStreet(orderStreet);

            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = format.parse(orderExpectedDate);
            } catch (ParseException e) {
            }
            order.setExpectedDate(date);
            order.setAppliance(orderAppliance);
            order.setUserId(orderUserId);
            order.setMemo(orderMemo);
            if ((access & C) == C || (access & c)==c) {
                LOGGER.debug("creating new order "+order);
                // update user data into database
                ordersService.updateOrder(order);
                LOGGER.debug("new order "+order+" was created");
            }
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("new order "+order+" did not create");
            LOGGER.error(e);
        }
    }
}
