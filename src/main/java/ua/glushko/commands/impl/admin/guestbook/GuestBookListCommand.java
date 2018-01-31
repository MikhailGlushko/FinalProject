package ua.glushko.commands.impl.admin.guestbook;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.services.impl.GuestBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_GUEST_BOOKS_LIST;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_GUEST_BOOKS_LIST_TITLE;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PATH_PAGE_GUEST_BOOK;

/**
 * Command that prepares data for show GuestBook
 * @version 1.0
 * @author Mikhail Glushko
 * @see GuestBook
 * @see GuestBookService
 *
 */
public class GuestBookListCommand implements Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        GuestBookService guestBookService = GuestBookService.getService();
        try {
            prepareDataToShow(request, guestBookService);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_GUEST_BOOK);
        return new CommandRouter(request, response, page);
    }

    private void prepareDataToShow(HttpServletRequest request, GuestBookService guestBookService) throws SQLException, TransactionException, ParameterException {
        int pageNumber = getPageNumber(request);
        List<GuestBook> items = guestBookService.getGuestBookList(pageNumber);
        List<String> titles = guestBookService.getGuestBookTitles();
        int count = guestBookService.count();
        storeGuestBookData(request, pageNumber, items, titles, count);
        LOGGER.debug("GuestBook items list were got and try to show");
    }

    private void storeGuestBookData(HttpServletRequest request, int pageNumber, List<GuestBook> items, List<String> titles, int count) throws ParameterException {
        int access = Authentication.checkAccess(request);
        int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        int rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        count = (count % rowsCount != 0) ? count / rowsCount + 1 : count / rowsCount;
        request.setAttribute(PARAM_GUEST_BOOKS_LIST_TITLE, titles);
        request.setAttribute(PARAM_GUEST_BOOKS_LIST, items);
        request.setAttribute(PARAM_LAST_PAGE, count);
        request.setAttribute(PARAM_PAGES_COUNT, pagesCount);
        request.setAttribute(PARAM_ROWS_COUNT, rowsCount);
        request.setAttribute(PARAM_PAGE, pageNumber);
        request.setAttribute(PARAM_ACCESS, access);
    }

    private int getPageNumber(HttpServletRequest request) {
        int pageNumber = 1;
        String parameter = request.getParameter(PARAM_PAGE);
        if (!(parameter == null || parameter.isEmpty() || parameter.equals("null")))
            pageNumber = Integer.valueOf(request.getParameter(PARAM_PAGE));
        return pageNumber;
    }
}