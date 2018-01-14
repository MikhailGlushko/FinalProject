package ua.glushko.commands.impl.admin.orders;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/** перенаправление на форму добавления нового пользователя при нажатии кнопки "+" на форме со списком пользователей*/
public class OrderAddCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        RepairServicesService repairServices = RepairServicesService.getService();
        try {
            List<RepairService> repairServiceList = repairServices.getRepairServiceList();
            List<Object[]> serviceList =  prepareList(repairServiceList);
            request.getSession().setAttribute(OrdersCommandHelper.PARAM_NAME_SERVICE_LIST, repairServiceList);
            request.getSession().setAttribute("serviceList",serviceList);
        } catch (PersistException | TransactionException e) {
            LOGGER.error(e);
        }
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