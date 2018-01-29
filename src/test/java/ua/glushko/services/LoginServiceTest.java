package ua.glushko.services;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LoginServiceTest {

    private UsersService loginService;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        loginService = UsersService.getService();
    }

    @Test (expected = DaoException.class)
    public void loginWrong() throws DatabaseException {
        try {
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("misha", "admin");
            User user = userListMap.keySet().iterator().next();
            List<Grant> grants = userListMap.get(user);
            assertNull(grants);
            assertNull("Метод должен вернуть исключение",userListMap);
        } catch (DaoException e) {
            throw new DaoException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    @Test( expected = DaoException.class)
    public void loginOk() throws DatabaseException {
        try {
            //String md5Hex = DigestUtils.md5Hex("admin");
            //System.out.println(md5Hex);
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("admin", "admin");
            User user = userListMap.keySet().iterator().next();
            List<Grant> grants = userListMap.get(user);
            assertNull(grants);
            assertNotNull("Метод должен вернуть обїект",userListMap);
        } catch (DaoException e) {
            throw new DaoException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }
}