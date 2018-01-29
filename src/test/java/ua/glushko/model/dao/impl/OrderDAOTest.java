package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DaoException;
import ua.glushko.model.entity.OrderStats;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.transaction.ConnectionPool;

import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
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
    }

    @Test
    public void readAll() throws DaoException {
        List<Order> read = dao.read();
        assertNotNull(read);
    }

    @Test
    public void readById() throws DaoException {
        Order read = dao.read(1);
        assertNotNull(read);
    }

    @Test
    public void readByCustomer() throws DaoException {
        List<Order> orders = dao.read(0, 100, 5);
        assertNotNull(orders);
    }

    @Test
    public void readByEmployee() throws DaoException {
        List<Order> orders = dao.read(0, 100, 0);
        assertNotNull(orders);
    }


    @Test
    public void create() throws DaoException {
        Order order = new Order();
        dao.create(order);
        order = new Order();
        order.setOrderDate(new Date(System.currentTimeMillis()-100000000));
        order.setExpectedDate(new Date(System.currentTimeMillis()+100000000));
        dao.create(order);
    }

    @Test
    public void readLimit() throws DaoException {
        List<Order> read = dao.read(0, 2);
        assertTrue(read.size()==2);
    }

    @Test
    public void takeNew() throws SQLException {
        Order order = dao.take(OrderStatus.NEW);
        System.out.println(order);
    }

    @Test
    public void getTotal() throws DaoException {
        Map<OrderStatus, Map<OrderStats,Integer>> total = dao.getTotal(4);
        total.entrySet().stream().forEach(item ->{System.out.println(item.getKey()+" "+item.getValue());});
        total = dao.getTotal(3);
        total.entrySet().stream().forEach(item ->{System.out.println(item.getKey()+" "+item.getValue());});
        total = dao.getTotal(2);
        total.entrySet().stream().forEach(item ->{System.out.println(item.getKey()+" "+item.getValue());});
    }
}