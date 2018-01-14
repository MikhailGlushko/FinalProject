package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.authentification.Authentification.u;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_USER_ID;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_USER_NAME;

/** Отображение информации о виде сервиса с возможностью редактирования или удаления */
public class OrderReadCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeOrderDetailToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeOrderDetailToSession(HttpServletRequest request) throws PersistException, TransactionException {
        HttpSession session = request.getSession();
        Integer userId = Integer.valueOf(session.getAttribute(Authentification.PARAM_NAME_ID).toString());
        int access = Authentification.checkAccess(request);

        // список заказов
        Integer id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
        OrdersService ordersService = OrdersService.getService();
        Order order = ordersService.getOrderById(id);
        List<String> titles = ordersService.getOrderTitles();

        // данные по пользователю
        UsersService usersService = UsersService.getService();
        User client = usersService.getUserById(order.getUserId());
        Integer clientId = client.getId();
        String clientName = client.getName();

        // получить список всех сотрудников
        List<User> stuffs = usersService.getUsersAsStuff(UserRole.CUSTOMER, false);
        if ((access & U) == U) {
            // изменение всех заявок
            session.setAttribute(PARAM_NAME_ORDERS_USER_ID,clientId);
            session.setAttribute(PARAM_NAME_ORDERS_USER_NAME,clientName);
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS_LIST_TITLE, titles);
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, order);
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_EMPLOYEE_LIST,stuffs);
            // список сервисов
            RepairServicesService repairServices = RepairServicesService.getService();
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
        } else if ((access & u) == u){
            // изменение только своих заявок
            if(order.getUserId()==userId || order.getEmployeeId()==userId){
                session.setAttribute(PARAM_NAME_ORDERS_USER_ID,clientId);
                session.setAttribute(PARAM_NAME_ORDERS_USER_NAME,clientName);
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS_LIST_TITLE, titles);
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, order);
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_EMPLOYEE_LIST,stuffs);
                // список сервисов
                RepairServicesService repairServices = RepairServicesService.getService();
                List<RepairService> repairServiceList = repairServices.getRepairServiceList();
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
            }
        }
    }
}