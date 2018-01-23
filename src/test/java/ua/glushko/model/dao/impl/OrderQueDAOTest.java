package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.exception.DaoException;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.OrderQue;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.H2DataSource;

import static org.junit.Assert.assertNotNull;

public class OrderQueDAOTest {

    private static GenericDAO<OrderQue> dao;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        dao = MySQLDAOFactory.getFactory().getOrderQueDao();
    }

    @Test(expected = DaoException.class)
    public void updateNull() throws DaoException {
        OrderQue orderQue = new OrderQue();
        dao.update(orderQue);
    }

    @Test
    public void createNull() throws DaoException {
        OrderQue orderQue = new OrderQue();
        dao.create(orderQue);
        OrderQue read = dao.read(orderQue.getId());
        assertNotNull(read);
    }

}