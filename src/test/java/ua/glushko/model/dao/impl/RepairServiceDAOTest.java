package ua.glushko.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import ua.glushko.transaction.H2DataSource;

public class RepairServiceDAOTest {

    private static GenericDAO<RepairService> reapirServiceDAO;

    @Before
    public void init() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
         reapirServiceDAO = DAOFactory.getFactory().getRepairServiceDao();
    }

    @Test
    public void readAll() throws DaoException {
        List<RepairService> list = reapirServiceDAO.read();
    }

    @Test
    public void create() throws DaoException {
        RepairService repairService = new RepairService();
        reapirServiceDAO.create(repairService);
        assertTrue(repairService.getId()!=0);
    }

    @Test
    public void update() throws DaoException {
        RepairService read = reapirServiceDAO.read(1);
        String name = read.getName();
        read.setName(name+"!");
        reapirServiceDAO.update(read);
        RepairService updated = reapirServiceDAO.read(1);
        String updatedName = updated.getName();
        assertNotEquals(name,updatedName);
    }

}