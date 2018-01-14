package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.Order;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.*;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class OrderDAOTest {

    private static GenericDAO<Order> dao;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
        dao = MySQLDAOFactory.getFactory().getOrderDao();
    }

    @Test
    public void readAll() throws PersistException {
        List<Order> read = dao.read();
        System.out.println(read);
    }

}