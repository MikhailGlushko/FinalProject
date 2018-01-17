package ua.glushko.commands.impl.admin.guestbook;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.GuestBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ua.glushko.authentification.Authentification.R;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_NAME_GUEST_BOOK_LIST;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PARAM_NAME_GUEST_BOOK_LIST_TITLE;
import static ua.glushko.commands.impl.admin.guestbook.GuestBookCommandHelper.PATH_PAGE_GUEST_BOOK;

public class GuestBookListCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            storeGuestBookListToSession(request);
        }catch (TransactionException | PersistException e) {
           LOGGER.error(e);
        }
        String page = ConfigurationManager.getProperty(PATH_PAGE_GUEST_BOOK);
        return new CommandRouter(request, response, page);
    }

    private void storeGuestBookListToSession(HttpServletRequest request) throws PersistException, TransactionException {
        try{
            HttpSession session = request.getSession();
            int access = Authentification.checkAccess(request);
            GuestBookService guestBookService = GuestBookService.getService();
            Integer pagesCount = null;
            Integer rowsCount = null;
            Integer pageNumber = null;
            Integer userId = null;
            try {
                pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
                rowsCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
                String parameter = request.getParameter(PARAM_NAME_PAGE);
                if (parameter == null || parameter.isEmpty())
                    pageNumber = 1;
                else
                    pageNumber = Integer.valueOf(request.getParameter(PARAM_NAME_PAGE));
                userId = Integer.valueOf(session.getAttribute(Authentification.PARAM_NAME_ID).toString());
            } catch (NumberFormatException e){
                LOGGER.debug(e);
            }
            if ((access & R)== R) {
                List<GuestBook> items = guestBookService.getGuestBookList(pageNumber, pagesCount, rowsCount);
                List<String> titles = guestBookService.getGuestBookTitles();
                int count = guestBookService.count();
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                session.setAttribute(PARAM_NAME_GUEST_BOOK_LIST_TITLE, titles);
                session.setAttribute(PARAM_NAME_GUEST_BOOK_LIST, items);
                session.setAttribute(PARAM_NAME_LAST_PAGE,count);
            } else if((access & Authentification.r) == Authentification.r) {
                List<GuestBook> items = guestBookService.getGuestBookList(pageNumber, pagesCount, rowsCount);
                List<String> titles = guestBookService.getGuestBookTitles();
                int count = guestBookService.count(userId);
                count = (count%rowsCount!=0)?count/rowsCount+1:count/rowsCount;
                session.setAttribute(PARAM_NAME_GUEST_BOOK_LIST_TITLE, titles);
                session.setAttribute(PARAM_NAME_GUEST_BOOK_LIST, items);
                session.setAttribute(PARAM_NAME_LAST_PAGE,count);
            }
                session.setAttribute(PARAM_NAME_PAGES_COUNT, pagesCount);
                session.setAttribute(PARAM_NAME_ROWS_COUNT, rowsCount);
                session.setAttribute(PARAM_NAME_PAGE, pageNumber);
                session.setAttribute(PARAM_NAME_ACCESS,access);
                LOGGER.debug("GuestBook items list were getted and try to show");
        } catch (NullPointerException e){
            LOGGER.debug("GuestBook items  list were not get.");
            LOGGER.error(e);
        }
    }
}