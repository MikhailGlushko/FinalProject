package ua.glushko.commands.impl.admin.guestbook;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.utils.Authentication;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.services.impl.GuestBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.COMMAND_GUEST_BOOK;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;

/**
 * Command that prepares data and create new record infor show GuestBook record in Database
 * @version 1.0
 * @author Mikhail Glushko
 * @see GuestBook
 * @see GuestBookService
 */
public class GuestBookNewCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        GuestBookService guestBookService = GuestBookService.getService();
        try {
            createGuestBook(request, guestBookService);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        String page = PARAM_SERVLET_PATH + "?command=" + COMMAND_GUEST_BOOK;
        return new CommandRouter(request, response, page);

    }

    private void createGuestBook(HttpServletRequest request, GuestBookService guestBookService) throws ParameterException, TransactionException, SQLException {
        GuestBook guestBook = prepareGuestBookDataForCreate(request);
        guestBookService.updateGuestBook(guestBook);
    }

    private GuestBook prepareGuestBookDataForCreate(HttpServletRequest request) throws ParameterException {
        if(Objects.isNull(request.getSession().getAttribute(Authentication.PARAM_ID)))
            throw  new ParameterException("user.didn't.login");
        int userId = Integer.valueOf(request.getSession().getAttribute(Authentication.PARAM_ID).toString());
        request.getSession().getAttribute(Authentication.PARAM_ID);
        String userName = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_USER_NAME);
        if(Objects.isNull(userName))
            throw new ParameterException("user.name.not.present");
        String description = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_SUBJECT);
        if(Objects.isNull(description))
            throw new ParameterException("description.not.present");
        String memo = request.getParameter(GuestBookCommandHelper.PARAM_GUEST_BOOK_MEMO);
        if(Objects.isNull(memo))
            throw new ParameterException("memo.not.present");
        GuestBook guestBook = new GuestBook();
        guestBook.setUserId(userId);
        guestBook.setUserName(userName);
        guestBook.setDescription(description);
        guestBook.setMemo(memo);
        guestBook.setActionDate(new Date(System.currentTimeMillis()));
        return guestBook;
    }
}
