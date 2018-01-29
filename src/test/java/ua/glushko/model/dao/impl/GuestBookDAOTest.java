package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.*;
import ua.glushko.transaction.H2DataSource;

public class GuestBookDAOTest {

    private GuestBookDAO instance;
    @Before
    public void init(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        instance = GuestBookDAO.getInstance();
    }

    @Test
    public void getInstance() {
        GuestBookDAO instance = GuestBookDAO.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void getTableName() {
        String tableName = instance.getTableName();
        assertEquals("guest_book",tableName);
    }

    @Test
    public void getFieldList() {
        String fieldList = instance.getFieldList();
        assertNotNull(fieldList);
    }

    @Test
    public void read() throws DaoException {
        List<GuestBook> read = instance.read();
        assertNotNull(read);
    }

    @Test
    public void readLimit() throws DaoException {
        List<GuestBook> read = instance.read(0,1);
        assertTrue(read.size()==1);
    }

    @Test
    public void create() throws DaoException {
        GuestBook guestBook = new GuestBook();
        instance.create(guestBook);
        assertTrue(guestBook.getId()!=0);
    }

    @Test
    public void update() throws DaoException {
        GuestBook guestBook = instance.read(1);
        String decription = guestBook.getDescription();
        guestBook.setDescription(decription+"!");
        instance.update(guestBook);
        GuestBook read = instance.read(1);
        String readDecription = read.getDescription();
        assertNotEquals(decription,readDecription);
    }
}