package ua.glushko.commands.impl.admin.guestbook;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.DatabaseException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.GuestBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

import static ua.glushko.commands.CommandFactory.COMMAND_GUEST_BOOK;

/** Создание новую запись в книге отзывов */
public class GuestBookNewCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            storeDataToDatabase(request);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = "/do?command=" + COMMAND_GUEST_BOOK;
        return new CommandRouter(request, response, page);

    }

    private void storeDataToDatabase(HttpServletRequest request) throws TransactionException, DatabaseException {
            String userName = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_USER_NAME);
            String description = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_SUBJECT);
            String memo = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_MEMO);

            GuestBook guestBook = new GuestBook();
            guestBook.setUserName(userName);
            guestBook.setDescription(description);
            guestBook.setMemo(memo);
            guestBook.setActionDate(new Date(System.currentTimeMillis()));

            GuestBookService guestBookService = GuestBookService.getService();
            guestBookService.updateGuestBook(guestBook);
    }
}
