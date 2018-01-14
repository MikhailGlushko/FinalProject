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

import static ua.glushko.authentification.Authentification.D;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS;

/** Удаление существующего пользователя */
public class OrderDeleteCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            deleteOrderDataFromDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_ORDERS + "&page=" + request.getSession().getAttribute(PARAM_NAME_PAGE);
        return new CommandRouter(request, response, page);
    }

    private void deleteOrderDataFromDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer Id = null;
        Order order = null;
        try {
            int access = Authentification.checkAccess(request);
            OrdersService ordersService = OrdersService.getService();
            Id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
            if ((access & D) == D) {
                LOGGER.debug("deleting order " + Id);
                // update user data into database
                order = ordersService.getOrderById(Id);
                ordersService.deleteOrder(Id);
                LOGGER.debug("service "+order+" was deleted");
            }
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_ORDERS);
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("order "+order+" did not delete");
            LOGGER.error(e);
        }
    }
}
