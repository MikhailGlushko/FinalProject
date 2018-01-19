package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GuestBookDAO;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.util.List;

public class GuestBookService extends AbstractService {

    private GuestBookService() {
    }

    public static GuestBookService getService() {
        return new GuestBookService();
    }

    public List<GuestBook> getGuestBookList() throws PersistException, TransactionException {
        GuestBookDAO guestBookDAO = MySQLDAOFactory.getFactory().getGuestBookDAO();
        List<GuestBook> read;
        try{
            TransactionManager.beginTransaction();
            read = guestBookDAO.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<GuestBook> getGuestBookList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        GuestBookDAO guestBookDAO = MySQLDAOFactory.getFactory().getGuestBookDAO();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<GuestBook> read;
        try {
            TransactionManager.beginTransaction();
            read = guestBookDAO.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getGuestBookTitles() {
        return  MySQLDAOFactory.getFactory().getGuestBookDAO().getTableHead();
    }

    public GuestBook getGuestBookById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getGuestBookDAO(),id);
    }

    public void updateGuestBook(GuestBook item) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getGuestBookDAO(),item);
    }

    public void deleteGuestBook(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getGuestBookDAO(),serviceId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getGuestBookDAO());
    }

    public int count(int id) throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getGuestBookDAO(),id);
    }
}
