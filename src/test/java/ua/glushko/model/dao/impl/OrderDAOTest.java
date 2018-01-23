package ua.glushko.model.dao.impl;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import ua.glushko.transaction.H2DataSource;

public class OrderDAOTest {

    private static OrderDAO dao;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        dao = MySQLDAOFactory.getFactory().getOrderDao();
    }

    @Test
    public void readAll() throws DaoException {
        List<Order> read = dao.read();
    }

    @Test
    public void readById() throws DaoException {
        Order read = dao.read(1);
    }

    @Test
    public void readByCustomer() throws DaoException {
        List<Order> orders = ((OrderDAO) dao).read(0, 100, 5);
    }

    @Test
    public void readByEmployee() throws DaoException {
        List<Order> orders = ((OrderDAO) dao).read(0, 100, 0);
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
        Order order = dao.takeNew();
        System.out.println(order);
    }
}