package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;

import java.util.List;

public class GuestBookService extends AbstractService {

    private GuestBookService() {
    }

    public static GuestBookService getService() {
        return new GuestBookService();
    }

    public List<GuestBook> getGuestBookList() throws PersistException, TransactionException {
        return (List<GuestBook>) getList(MySQLDAOFactory.getFactory().getGuestBookDAO());
    }

    public List<GuestBook> getGuestBookList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<GuestBook>) getList(MySQLDAOFactory.getFactory().getGuestBookDAO(),page,pagesCount,rowsPerPage);
    }

    public List<GuestBook> getGuestBookList(int page, int pagesCount, int rowsPerPage, int userId) throws PersistException, TransactionException {
        return (List<GuestBook>) getList(MySQLDAOFactory.getFactory().getGuestBookDAO(),page,pagesCount,rowsPerPage,userId);
    }

    public List<String> getGuestBookTitles() {
        return  MySQLDAOFactory.getFactory().getGuestBookDAO().getTableHead();
    }

    public GuestBook getGuestBookById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getGuestBookDAO(),id);
    }

    public void updateGuestBookHistoty(GuestBook item) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getGuestBookDAO(),item);
    }

    public void deleteGuestBook(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getGuestBookDAO(),serviceId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getGuestBookDAO());
    }
}
