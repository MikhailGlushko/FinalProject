package ua.glushko.model.dao;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionPool;

import java.security.spec.ECField;
import java.util.List;

import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class GrantDAOTest {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getSimpleName());

    private static GenericDAO<Grant> grantDAO;

    @Before
    public void init() {
            ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
            grantDAO = MySQLDAOFactory.getFactory().getGrantDao();
    }

    @Test
    public void read() throws PersistException {
        List<Grant> grants = ((GrantDAO) grantDAO).read("ADMIN");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void create() throws PersistException {
        Grant grant = new Grant();
        grantDAO.create(grant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void update() throws PersistException {
        Grant grant = grantDAO.read(1);
        grantDAO.update(grant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws PersistException {
        Grant grant = grantDAO.read(1);
        grantDAO.delete(grant.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteAll() throws PersistException {
        grantDAO.deleteAll();
    }

}