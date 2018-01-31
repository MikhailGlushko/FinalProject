package ua.glushko.services.impl;

import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;

import java.sql.SQLException;
import java.util.List;

import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_PAGES_COUNT;
import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_ROWS_COUNT;

/**
 * GuestBook services
 * @author Mikhail Glushko
 * @version 1.0
 */
public class GuestBookService extends Service {

    private GuestBookService() {
    }

    public static GuestBookService getService() {
        return new GuestBookService();
    }

    /** List of all GuestBooks*/
    public List<GuestBook> getGuestBookList() throws SQLException {
        return DAOFactory.getFactory().getGuestBookDAO().read();
    }

    /** List of GuestBook with limit*/
    public List<GuestBook> getGuestBookList(int page) throws SQLException {
        int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        int rowsPerPage = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        return DAOFactory.getFactory().getGuestBookDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names*/
    public List<String> getGuestBookTitles() {
        return  DAOFactory.getFactory().getGuestBookDAO().getTableHead();
    }

    /** Get GuestBook by id */
    public GuestBook getGuestBookById(int id) throws SQLException {
        return getById(DAOFactory.getFactory().getGuestBookDAO(),id);
    }

    /** Update exist GuestBook or create new*/
    public void updateGuestBook(GuestBook item) throws TransactionException, SQLException {
        update(DAOFactory.getFactory().getGuestBookDAO(),item);
    }

    /** Delete exist GuestBook */
    public void deleteGuestBook(Integer serviceId) throws TransactionException, SQLException {
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
