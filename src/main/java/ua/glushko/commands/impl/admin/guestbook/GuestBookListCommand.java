package ua.glushko.commands.impl.admin.guestbook;

import ua.glushko.services.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.GuestBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

import static ua.glushko.services.utils.Authentication.R;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_GUEST_BOOKS_LIST;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_GUEST_BOOKS_LIST_TITLE;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PATH_PAGE_GUEST_BOOK;

public class GuestBookListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeGuestBookListToSession(request);
        }catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_GUEST_BOOK);
        return new CommandRouter(request, response, page);
    }

    private void storeGuestBookListToSession(HttpServletRequest request) throws SQLException, TransactionException, ParameterException {
            HttpSession session = request.getSession();
            int access = Authentication.checkAccess(request);
            GuestBookService guestBookService = GuestBookService.getService();
            int pagesCount = 0;
            int rowsCount = 0;
            int pageNumber = 1;
            Integer userId = null;
            try {
                pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
                rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                String parameter = request.getParameter(PARAM_PAGE);
                if (!(parameter == null || parameter.isEmpty() || parameter.equals("null")))
                    pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
                userId = Integer.valueOf(session.getAttribute(Authentication.PARAM_ID).toString());
            } catch (NumberFormatException e){
                LOGGER.debug(e);
            }
            if ((access & R)== R) {
                List<GuestBook> items = guestBookService.getGuestBookList(pageNumber, pagesCount, rowsCount);
                List<String> titles = guestBookService.getGuestBookTitles();
                int count = guestBookService.count();
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                request.setAttribute(PARAM_GUEST_BOOKS_LIST_TITLE, titles);
                request.setAttribute(PARAM_GUEST_BOOKS_LIST, items);
                request.setAttribute(PARAM_LAST_PAGE,count);
            } else if((access & Authentication.r) == Authentication.r) {
                List<GuestBook> items = guestBookService.getGuestBookList(pageNumber, pagesCount, rowsCount);
                List<String> titles = guestBookService.getGuestBookTitles();
                int count = guestBookService.count(userId);
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                request.setAttribute(PARAM_GUEST_BOOKS_LIST_TITLE, titles);
                request.setAttribute(PARAM_GUEST_BOOKS_LIST, items);
                request.setAttribute(PARAM_LAST_PAGE,count);
            }
                request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
                request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
                request.setAttribute(PARAM_PAGE, pageNumber);
                request.setAttribute(PARAM_ACCESS,access);
                LOGGER.debug("GuestBook items list were got and try to show");
    }
}