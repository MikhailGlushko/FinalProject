package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class GuestBookServiceTest {

    GuestBookService service;
    @Before
    @Test
    public void getService() {
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
        service = GuestBookService.getService();
    }

    @Test
    public void getGuestBookList() throws DaoException, TransactionException, DatabaseException {
        List<GuestBook> guestBookList = service.getGuestBookList();
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookList1() throws DaoException, TransactionException, DatabaseException {
        List<GuestBook> guestBookList = service.getGuestBookList(1, 1, 1);
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookTitles() throws DaoException, TransactionException, DatabaseException {
        service.getGuestBookList(1, 1, 1);
        List<String> guestBookTitles = service.getGuestBookTitles();
        assertNotNull(guestBookTitles);
    }

    @Test
    public void getGuestBookById() throws DaoException, TransactionException {
        GuestBook book = service.getGuestBookById(1);
        assertNotNull(book);
    }

    @Test
    public void updateGuestBook() throws DaoException, TransactionException, DatabaseException {
        GuestBook book = service.getGuestBookById(1);
        assertNotNull(book);
        String decription = book.getDescription();
        book.setDescription(decription+"!");
        service.updateGuestBook(book);
        book = service.getGuestBookById(1);
        String updated = book.getDescription();
        assertNotNull(decription,updated);
    }

    @Test
    public void deleteGuestBook() throws DaoException, TransactionException, DatabaseException {
        GuestBook book = service.getGuestBookById(1);
        book.setId(0);
        service.updateGuestBook(book);
        Integer id = book.getId();
        service.deleteGuestBook(id);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
    }
}