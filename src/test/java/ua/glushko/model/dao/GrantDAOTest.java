package ua.glushko.model.dao;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.exception.DaoException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static ua.glushko.model.dao.H2DataSource.H2_CONNECTION_POOL;

public class GrantDAOTest {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getSimpleName());

    private static GenericDAO<Grant> grantDAO;

    @Before
    public void init() {
            ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
            grantDAO = MySQLDAOFactory.getFactory().getGrantDao();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readAll() throws DaoException {
        List<Grant> grants = ((GrantDAO) grantDAO).read();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteOne() throws DaoException {
        grantDAO.delete(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateOne() throws DaoException {
        List<Grant> grants = ((GrantDAO) grantDAO).read("ADMIN");
        assertNotNull(grants);
        Grant grant = grants.iterator().next();
        ((GrantDAO) grantDAO).update(grant);
    }

    @Test
    public void read() throws DaoException {
        List<Grant> grants = ((GrantDAO) grantDAO).read("ADMIN");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void create() throws DaoException {
        Grant grant = new Grant();
        grantDAO.create(grant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void update() throws DaoException {
        Grant grant = grantDAO.read(1);
        grantDAO.update(grant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws DaoException {
        Grant grant = grantDAO.read(1);
        grantDAO.delete(grant.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteAll() throws DaoException {
        grantDAO.deleteAll();
    }

}