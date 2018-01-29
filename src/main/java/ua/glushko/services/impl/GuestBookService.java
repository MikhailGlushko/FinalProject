package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;

import java.sql.SQLException;
import java.util.List;

public class GuestBookService extends Service {

    private GuestBookService() {
    }

    public static GuestBookService getService() {
        return new GuestBookService();
    }

    /** List of all GuestBooks*/
    public List<GuestBook> getGuestBookList() throws DatabaseException {
        return DAOFactory.getFactory().getGuestBookDAO().read();
    }

    /** List of GuestBook with limit*/
    public List<GuestBook> getGuestBookList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getGuestBookDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names*/
    public List<String> getGuestBookTitles() {
        return  DAOFactory.getFactory().getGuestBookDAO().getTableHead();
    }

    /** Get GuestBook by id */
    public GuestBook getGuestBookById(int id) throws DaoException {
        return getById(DAOFactory.getFactory().getGuestBookDAO(),id);
    }

    /** Update exist GuestBook or create new*/
    public void updateGuestBook(GuestBook item) throws TransactionException, DatabaseException {
        update(DAOFactory.getFactory().getGuestBookDAO(),item);
    }

    /** Delete exist GuestBook */
    public void deleteGuestBook(Integer serviceId) throws TransactionException, DatabaseException {
        delete(DAOFactory.getFactory().getGuestBookDAO(),serviceId);
    }

    /** get total of GuestBooks */
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getGuestBookDAO());
    }

    /** Get total of GuestBook*/
    public int count(int id) throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getGuestBookDAO(),id);
    }
}
