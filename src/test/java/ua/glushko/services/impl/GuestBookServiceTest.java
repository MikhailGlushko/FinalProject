package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

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
    public void getGuestBookList() throws PersistException, TransactionException {
        List<GuestBook> guestBookList = service.getGuestBookList();
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookList1() throws PersistException, TransactionException {
        List<GuestBook> guestBookList = service.getGuestBookList(1, 1, 1);
        assertNotNull(guestBookList);
    }

    @Test
    public void getGuestBookTitles() throws PersistException, TransactionException {
        service.getGuestBookList(1, 1, 1);
        List<String> guestBookTitles = service.getGuestBookTitles();
        assertNotNull(guestBookTitles);
    }

    @Test
    public void getGuestBookById() throws PersistException, TransactionException {
        GuestBook book = service.getGuestBookById(1);
        assertNotNull(book);
    }

    @Test
    public void updateGuestBook() throws PersistException, TransactionException {
        GuestBook book = service.getGuestBookById(1);
        assertNotNull(book);
        String decription = book.getDecription();
        book.setDecription(decription+"!");
        service.updateGuestBook(book);
        book = service.getGuestBookById(1);
        String updated = book.getDecription();
        assertNotNull(decription,updated);
    }

    @Test
    public void deleteGuestBook() throws PersistException, TransactionException {
        GuestBook book = service.getGuestBookById(1);
        book.setId(0);
        service.updateGuestBook(book);
        Integer id = book.getId();
        service.deleteGuestBook(id);
    }

    @Test
    public void count() throws PersistException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
    }

    @Test
    public void count1() {
    }

    @Test
    public void getList() {
    }

    @Test
    public void getList1() {
    }

    @Test
    public void getById() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void count2() {
    }

    @Test
    public void count3() {
    }
}