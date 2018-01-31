package ua.glushko.commands.impl.orders;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.ParameterException;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.D;
import static ua.glushko.commands.CommandFactory.COMMAND_ORDERS;

/**
 * Admin Order Management Command, which receives data from the form and delete record from database
 * @author Mikhail Glushko
 * @version 1.0
 * @see Order
 * @see OrdersService
 */
public class OrderDeleteCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        OrdersService ordersService = OrdersService.getService();
        try {
            if (isUserHasRightToDelete(request)){
                Order order = deleteOrder(request, ordersService);
                LOGGER.debug("service " + order + " was deleted");
            } else {
                LOGGER.debug("user.don't.have.rights.to.delete");
            }
            request.setAttribute(PARAM_COMMAND, COMMAND_ORDERS);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_ORDERS + "&page=" + request.getAttribute(PARAM_PAGE);
        return new CommandRouter(request, response, page);
    }

    private Order deleteOrder(HttpServletRequest request, OrdersService ordersService) throws ParameterException, SQLException, TransactionException {
        Integer id = getOrderId(request);
        LOGGER.debug("deleting order " + id);
        Order order = ordersService.getOrderById(id);
        if(Objects.nonNull(order))
            ordersService.deleteOrder(id);
        return order;
    }

    private Integer getOrderId(HttpServletRequest request) throws ParameterException {
        if (Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
            throw new ParameterException("order.id.not.present");
        return Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
    }

    private boolean isUserHasRightToDelete(HttpServletRequest request) throws ParameterException {
        return (Authentication.checkAccess(request) & D)==D;
    }
}
