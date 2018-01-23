package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionPool;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import ua.glushko.transaction.H2DataSource;

public class OrderHistoryDAOTest {

    OrderHistoryDAO dao;

    @Before
    public void init(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        dao = OrderHistoryDAO.getInstance();
    }

    @Test
    public void read() throws DaoException {
        List<OrderHistory> read = dao.read();
        assertNotNull(read);
    }

    @Test
    public void readLimit() throws DaoException {
        List<OrderHistory> read = dao.read(0, 1);
        assertTrue(read.size()==1);
    }

    @Test
    public void read2() throws DaoException {
        OrderHistory read = dao.read(1);
        assertNotNull(read);
    }

    @Test
    public void read3() throws DaoException {
        List<OrderHistory> read = dao.read(0, 1, 1);
        assertNotNull(read);
    }

    @Test (expected = DaoException.class)
    public void createNull() throws DaoException {
        OrderHistory orderHistory= new OrderHistory() ;
        dao.create(orderHistory);
        assertTrue(orderHistory.getId()!=0);
    }

    @Test
    public void create() throws DaoException {
        OrderHistory orderHistory= new OrderHistory() ;
        orderHistory.setUserId(1);
        orderHistory.setOrderId(1);
        orderHistory.setAction(Action.ADD_COMMENT.name());
        orderHistory.setDescription(Action.ADD_COMMENT.name());
        orderHistory.setActionDate(new Date(System.currentTimeMillis()));
        orderHistory.setOldValue(Action.ADD_COMMENT.name());
        orderHistory.setNewValue(Action.ADD_COMMENT.name());
        dao.create(orderHistory);
        assertTrue(orderHistory.getId()!=0);
    }
}