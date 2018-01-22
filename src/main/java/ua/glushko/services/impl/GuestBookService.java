package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GuestBookDAO;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.DaoException;
import ua.glushko.model.exception.DatabaseException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.List;

public class GuestBookService extends AbstractService {

    private GuestBookService() {
    }

    public static GuestBookService getService() {
        return new GuestBookService();
    }

    public List<GuestBook> getGuestBookList() throws TransactionException, DatabaseException {
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

    public List<GuestBook> getGuestBookList(int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
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

    public GuestBook getGuestBookById(int id) throws DaoException {
        return getById(MySQLDAOFactory.getFactory().getGuestBookDAO(),id);
    }

    public void updateGuestBook(GuestBook item) throws TransactionException, DatabaseException {
        update(MySQLDAOFactory.getFactory().getGuestBookDAO(),item);
    }

    public void deleteGuestBook(Integer serviceId) throws TransactionException, DatabaseException {
        delete(MySQLDAOFactory.getFactory().getGuestBookDAO(),serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getGuestBookDAO());
    }

    public int count(int id) throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getGuestBookDAO(),id);
    }
}
