package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.dao.impl.OrderHistoryDAO;
import ua.glushko.model.entity.*;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersHistoryService;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.*;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.*;

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
        Integer pagesCount = null;
        Integer rowsCount = null;
        Integer pageNumber = null;
        Integer id = null;

        try {
            pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_NAME_PAGE);
            if (parameter == null || parameter.isEmpty())
                pageNumber = 1;
            else
                pageNumber = Integer.valueOf(request.getParameter(PARAM_NAME_ORDERS_HISTORY_PAGE));
            id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
        } catch (NumberFormatException e){
            LOGGER.error(e);
        }
        int access = Authentification.checkAccess(request);

        // список заказов
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
        if ((access & R) == R || (access & r) == r) {
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
            // список истории
            OrdersHistoryService ordersHistoryService = OrdersHistoryService.getService();
            List<OrderHistory> orderHistoryList = ordersHistoryService.getOrderHistoryList(pageNumber, pagesCount, rowsCount);
            List<String> orderHistoryTitles = ordersHistoryService.getOrderHistoryTitles();
            session.setAttribute(PARAM_NAME_ORDERS_HISTORY_LIST,orderHistoryList);
            session.setAttribute(PARAM_NAME_ORDERS_HISTORY_LIST_TITLE,orderHistoryTitles);
        }
    }
}