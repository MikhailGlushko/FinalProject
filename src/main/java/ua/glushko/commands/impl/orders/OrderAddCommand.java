package ua.glushko.commands.impl.orders;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.User;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

import static ua.glushko.commands.impl.orders.OrdersCommandHelper.PARAM_ORDER_USER_ID;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.PARAM_ORDER_USER_NAME;

/** redirect to the form of adding a new order by pressing the "+" button on the form with the list of users*/
public class OrderAddCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {

            // prepare list for menu
            RepairServicesService repairServices = RepairServicesService.getService();
            int userId = 0;
            try {
                userId = Integer.valueOf(request.getSession().getAttribute(Authentication.PARAM_ID).toString());
            } catch (NumberFormatException e){
                LOGGER.error(e);
            }
            UsersService usersService = UsersService.getService();
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            List<Object[]> serviceList =  prepareList(repairServiceList);

            // prepare user data who create request
            User client = usersService.getUserById(userId);
            Integer clientId = client.getId();
            String clientName = client.getName();

            // store data to the session
            request.setAttribute(PARAM_ORDER_USER_ID,clientId);
            request.setAttribute(PARAM_ORDER_USER_NAME,clientName);
            request.setAttribute(OrdersCommandHelper.PARAM_ORDER, new Order());
            request.setAttribute(OrdersCommandHelper.PARAM_SERVICES_LIST, repairServiceList);
            request.setAttribute("serviceList",serviceList);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        // forward to the form to create new request
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_ADD);
        return new CommandRouter(request, response, page);
    }

    private List<Object[]> prepareList(List<RepairService> repairServiceList) {
        List<Object[]> list = new LinkedList<>();
        for (RepairService item:repairServiceList) {
            list.add(new Object[]{item.getId(),item.getNameRu()});
        }
        return list;
    }
}