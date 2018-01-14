package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.Order;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class OrderDAOTest {

    private static OrderDAO dao;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        dao = MySQLDAOFactory.getFactory().getOrderDao();
    }

    @Test
    public void readAll() throws PersistException {
        List<Order> read = dao.read();
    }

    @Test
    public void readById() throws PersistException {
        Order read = dao.read(1);
    }

    @Test
    public void readByCustomer() throws PersistException {
        List<Order> orders = ((OrderDAO) dao).read(0, 100, 5);
    }

    @Test
    public void readByEmployee() throws PersistException {
        List<Order> orders = ((OrderDAO) dao).read(0, 100, 0);
    }


    @Test
    public void create() throws PersistException {
        Order order = new Order();
        dao.create(order);
        System.out.println(order);

        order = new Order();
        order.setOrderDate(new Date(System.currentTimeMillis()-100000000));
        order.setExpectedDate(new Date(System.currentTimeMillis()+100000000));
        dao.create(order);
        System.out.println(order);
    }

    @Test
    public void readLimit() throws PersistException {
        List<Order> read = dao.read(0, 2);
        System.out.println(read);
    }
}