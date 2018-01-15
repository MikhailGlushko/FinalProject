package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_USER_ID;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_USER_NAME;

/** перенаправление на форму добавления нового пользователя при нажатии кнопки "+" на форме со списком пользователей*/
public class OrderAddCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            // готовим список доступных сервисов для выпадающего меню
            RepairServicesService repairServices = RepairServicesService.getService();
            Integer userId = null;
            try {
                userId = Integer.valueOf(request.getSession().getAttribute(Authentification.PARAM_NAME_ID).toString());
            } catch (NumberFormatException e){
                LOGGER.error(e);
            }
            UsersService usersService = UsersService.getService();
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            List<Object[]> serviceList =  prepareList(repairServiceList);

            // готовим данніе о пользователе, которій инициировал новую заявку
            User client = usersService.getUserById(userId);
            Integer clientId = client.getId();
            String clientName = client.getName();

            // Сохраняем данніе в сессию
            request.getSession().setAttribute(PARAM_NAME_ORDERS_USER_ID,clientId);
            request.getSession().setAttribute(PARAM_NAME_ORDERS_USER_NAME,clientName);
            request.getSession().setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, new Order());
            request.getSession().setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
            request.getSession().setAttribute("serviceList",serviceList);
        } catch (PersistException | TransactionException e) {
            LOGGER.error(e);
        }

        // Передаем управдение на форму ввода новой заявки
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