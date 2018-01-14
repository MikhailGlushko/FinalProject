package ua.glushko.services;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import static org.junit.Assert.*;

public class RegisterServiceTest {

    private UsersService registerService;
    @Before
    @Test
    public void getService() {
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
        registerService = UsersService.getService();
    }

    @Test (expected = PersistException.class)
    public void registerExistLogin() throws PersistException {
        try {
            User user = registerService.register("admin", "admin", "admin", "admin", "admin","admin");
            assertNotNull("Должно было сработать исключение",user);
        } catch (PersistException e) {
            throw new PersistException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerNewUser() throws PersistException, TransactionException {
        try {
            User user = registerService.register("test10", "test10", "test10", "test10", "test10","test10");
            assertNotNull("",user);
        } catch (PersistException e) {
          throw new PersistException(e);
        }
    }

    @Test (expected = NullPointerException.class)
    public void registerUserWithNullParameters() throws TransactionException, PersistException {
        try {
            User user = registerService.register(null, "test10", "test10", "test10", "test10","test10");
            assertNotNull("",user);
        } catch (PersistException e) {
            throw new PersistException(e);
        }
    }
}