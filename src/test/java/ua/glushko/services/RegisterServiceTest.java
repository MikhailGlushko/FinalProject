package ua.glushko.services;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import static org.junit.Assert.*;

public class RegisterServiceTest {

    private UsersService registerService;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        registerService = UsersService.getService();
        assertNotNull(registerService);
    }

    @Test (expected = DaoException.class)
    public void registerExistLogin() throws DatabaseException {
        try {
            User user = registerService.register("admin", "admin", "admin", "admin","admin");
            assertNotNull("Должно было сработать исключение",user);
        } catch (DaoException e) {
            throw new DaoException(e);
        } catch (TransactionException | ParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerNewUser() throws TransactionException, ParameterException, DatabaseException {
        try {
            User user = registerService.register("test10", "test10", "test10", "test10","test10");
            assertNotNull("",user);
        } catch (DaoException e) {
          throw new DaoException(e);
        }
    }

    @Test (expected = ParameterException.class)
    public void registerUserWithNullParameters() throws TransactionException, ParameterException, DatabaseException {
        try {
            User user = registerService.register(null, "test10", "test10", "test10","test10");
            assertNotNull("",user);
        } catch (DaoException e) {
            throw new DaoException(e);
        }
    }
}