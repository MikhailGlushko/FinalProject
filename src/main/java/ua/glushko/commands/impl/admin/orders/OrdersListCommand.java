package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.R;
import static ua.glushko.authentification.Authentification.checkAccess;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_LIST;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_NAME_ORDERS_LIST_TITLE;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PATH_PAGE_ORDERS;

public class OrdersListCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServicesListToSession(request);
        }catch (TransactionException | PersistException e) {
           LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_ORDERS);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServicesListToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try{
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            OrdersService ordersService = OrdersService.getService();
            Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
            Integer rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
            String parameter = request.getParameter(PARAM_NAME_PAGE);
            Integer pageNumber;
            if(parameter==null || parameter.isEmpty())
                pageNumber = 1;
            else
                pageNumber = Integer.valueOf(request.getParameter(PARAM_NAME_PAGE));
            if ((access & R)== R) {
                List<Order> items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount);
                List<String> titles = ordersService.getOrderTitles();
                session.setAttribute(PARAM_NAME_ORDERS_LIST_TITLE, titles);
                session.setAttribute(PARAM_NAME_ORDERS_LIST, items);
            } else if((access & Authentification.r) == Authentification.r) {
                Integer userId = Integer.valueOf(session.getAttribute(Authentification.PARAM_NAME_ID).toString());
                List<Order> items = ordersService.getOrderList(pageNumber, pagesCount, rowsCount,userId);
                List<String> titles = ordersService.getOrderTitles();
                session.setAttribute(PARAM_NAME_ORDERS_LIST_TITLE, titles);
                session.setAttribute(PARAM_NAME_ORDERS_LIST, items);
            }
                session.setAttribute(PARAM_NAME_PAGES_COUNT, pagesCount);
                session.setAttribute(PARAM_NAME_ROWS_COUNT, rowsCount);
                session.setAttribute(PARAM_NAME_PAGE, pageNumber);
                session.setAttribute(PARAM_NAME_ACCESS,access);
        } catch (NullPointerException e){
            LOGGER.error(e);
        }
    }
}