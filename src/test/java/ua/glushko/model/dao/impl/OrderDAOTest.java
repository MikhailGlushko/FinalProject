package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DaoException;
import ua.glushko.model.entity.OrderStats;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.transaction.ConnectionPool;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import ua.glushko.transaction.H2DataSource;

public class OrderDAOTest {

    private static OrderDAO dao;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        dao = DAOFactory.getFactory().getOrderDao();
        assertNotNull(dao);
    }

    @Test
    public void readAll() throws SQLException {
        List<Order> read = dao.read();
        assertNotNull(read);
    }

    @Test
    public void readById() throws SQLException {
        Order read = dao.read(1);
        assertNotNull(read);
    }

    @Test
    public void readByCustomer() throws SQLException {
        List<Order> orders = dao.read(0, 100, 5);
        assertNotNull(orders);
    }

    @Test
    public void readByEmployee() throws SQLException {
        List<Order> orders = dao.read(0, 100, 0);
        assertNotNull(orders);
    }


    @Test
    public void create() throws SQLException {
        Order order = new Order();
        dao.create(order);
        order = new Order();
        order.setOrderDate(new Date(System.currentTimeMillis()-100000000));
        order.setExpectedDate(new Date(System.currentTimeMillis()+100000000));
        Integer countBefore = dao.count();
        dao.create(order);
        Integer countAfter = dao.count();
        assertTrue(!countAfter.equals(countBefore));
    }

    @Test
    public void readLimit() throws SQLException {
        List<Order> read = dao.read(0, 2);
        assertTrue(read.size()==2);
    }

    @Test
    public void takeNew() throws SQLException {
        Order read = dao.read(1);
        Order order = dao.take(OrderStatus.NEW);
        assertEquals(read,order);
    }

    @Test
    public void getTotal() throws SQLException {
        Map<OrderStatus, Map<OrderStats,Integer>> total = dao.getTotal(4);
        assertNotNull(total);
    }
}