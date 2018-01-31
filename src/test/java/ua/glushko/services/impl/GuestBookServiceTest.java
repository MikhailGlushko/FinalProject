package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class GuestBookServiceTest {

    private GuestBookService service;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        service = GuestBookService.getService();
        assertNotNull(service);
    }

    @Test
    public void getGuestBookList() throws SQLException {
        List<GuestBook> guestBookList = service.getGuestBookList();
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookList1() throws SQLException {
        List<GuestBook> guestBookList = service.getGuestBookList(1);
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookTitles() throws SQLException {
        service.getGuestBookList(1);
        List<String> guestBookTitles = service.getGuestBookTitles();
        assertNotNull(guestBookTitles);
    }

    @Test
    public void getGuestBookById() throws SQLException {
        GuestBook book = service.getGuestBookById(1);
        assertNotNull(book);
    }

    @Test
    public void updateGuestBook() throws TransactionException, SQLException {
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
    public void deleteGuestBook() throws TransactionException, SQLException {
        GuestBook book = service.getGuestBookById(1);
        book.setId(0);
        service.updateGuestBook(book);
        Integer id = book.getId();
        service.deleteGuestBook(id);
        GuestBook guestBook = service.getGuestBookById(id);
        assertNull(guestBook);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
        assertTrue(count!=0);
    }
}