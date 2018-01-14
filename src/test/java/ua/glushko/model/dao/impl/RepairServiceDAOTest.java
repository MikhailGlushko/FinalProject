package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class RepairServiceDAOTest {

    private static GenericDAO<RepairService> reapirServiceDAO;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
         reapirServiceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
    }

    @Test
    public void readAll() throws PersistException {
        List<RepairService> list = reapirServiceDAO.read();
    }

}