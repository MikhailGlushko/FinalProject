package ua.glushko.commands.impl;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.*;
import ua.glushko.services.impl.GuestBookService;
import ua.glushko.services.impl.NewsService;
import ua.glushko.services.impl.OrdersService;
import ua.glushko.services.impl.RepairServicesService;
import ua.glushko.commands.utils.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_GUEST_BOOKS_LIST;
import static ua.glushko.commands.impl.admin.news.NewsCommandHelper.PARAM_GUEST_NEWS_LIST;
import static ua.glushko.commands.impl.orders.OrdersCommandHelper.PARAM_ORDERS_STAT_TOTAL;
import static ua.glushko.commands.impl.admin.services.ServicesCommandHelper.PARAM_SERVICE_LIST;

public class WelcomeCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            GuestBookService guestBookService = GuestBookService.getService();
            List<GuestBook> guestBookList = guestBookService.getGuestBookList(1, 1, 3);
            request.setAttribute(PARAM_GUEST_BOOKS_LIST,guestBookList);

            NewsService newsService = NewsService.getService();
            List<News> newsList = newsService.getNewsList(1, 1, 3);
            request.setAttribute(PARAM_GUEST_NEWS_LIST,newsList);

            RepairServicesService repairServicesService = RepairServicesService.getService();
            List<RepairService> repairServiceList = repairServicesService.getRepairServiceList();
            request.setAttribute(PARAM_SERVICE_LIST,repairServiceList);

            OrdersService ordersService = OrdersService.getService();
            if(!Authentication.isUserLogIn(request.getSession()))
                throw new ParameterException("user.not.log.in");

            // Статистика заказов
            Map<OrderStatus, Map<OrderStats, Integer>> stats = ordersService.getStats(Authentication.getCurrentUserId(request.getSession()));
            request.setAttribute(PARAM_ORDERS_STAT_TOTAL, stats);

        } catch (Exception e) {
            LOGGER.error(e);
        }

        String page = ConfigurationManager.getProperty(PATH_PAGE_WELCOME);
        return new CommandRouter(request, response, page);
    }
}
