package ua.glushko.commands.impl.admin.orders;

import org.omg.IOP.ServiceContextHelper;
import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.authentification.Authentification.u;

/** Отображение информации о виде сервиса с возможностью редактирования или удаления */
public class OrderReadCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeRepairServiceDetailToSession(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(OrdersCommandHelper.PATH_PAGE_ORDERS_DETAIL);
        return new CommandRouter(request, response, page);
    }

    private void storeRepairServiceDetailToSession(HttpServletRequest request) throws PersistException, TransactionException {
        HttpSession session = request.getSession();
        int access = Authentification.checkAccess(request);
        Integer id = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
        OrdersService ordersService = OrdersService.getService();
        if ((access & U) == U) {
            Order item = ordersService.getOrderById(id);
            List<String> titles = ordersService.getOrderTitles();
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS_LIST_TITLE, titles);
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, item);
            RepairServicesService repairServices = RepairServicesService.getService();
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
        } else if ((access & u) == u){
            Order item = ordersService.getOrderById(id);
            List<String> titles = ordersService.getOrderTitles();
            Integer userId = Integer.valueOf(session.getAttribute(Authentification.PARAM_NAME_ID).toString());
            if(item.getUserId()==userId){
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS_LIST_TITLE, titles);
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, item);
                RepairServicesService repairServices = RepairServicesService.getService();
                List<RepairService> repairServiceList = repairServices.getRepairServiceList();
                session.setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
            }
        }
    }
}