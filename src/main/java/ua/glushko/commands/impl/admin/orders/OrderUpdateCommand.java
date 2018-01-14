package ua.glushko.commands.impl.admin.orders;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.RepairServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_ORDERS;

/** Обновление данных о пользователе после редактирования */
public class OrderUpdateCommand extends Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeServiceDataToDatabase(request);
        } catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_NAME_ORDERS;
        return new CommandRouter(request, response, page);

    }

    private void storeServiceDataToDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer serviceId = null;
        try {
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            serviceId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_NAME_ORDERS_ID));
//            String name = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME);
//            String nameRu = request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_NAME_RU);
//            Integer parent = Integer.valueOf(request.getParameter(ServicesCommandHelper.PARAM_NAME_SERVICE_PARENT));
            RepairServicesService service = RepairServicesService.getService();
            // get user data from database
            RepairService item = service.getRepairServiceById(serviceId);
//            item.setName(name);
//            item.setNameRu(nameRu);
//            item.setParent(parent);
            if ((access & U) == U) {
                service.updateRepairService(item);
            }
            session.setAttribute(OrdersCommandHelper.PARAM_NAME_ORDERS, item);
            request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_ORDERS);
            LOGGER.debug("service " + serviceId+" was updated");
        } catch (NullPointerException | NumberFormatException e) {
            LOGGER.debug("service " + serviceId+" was not update");
            LOGGER.error(e);
        }
    }
}
