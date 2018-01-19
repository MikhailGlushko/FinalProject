package ua.glushko.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
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
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
        loginService = UsersService.getService();
    }

    @Test (expected = PersistException.class)
    public void loginWrong() throws PersistException {
        try {
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("misha", "admin");
            User user = userListMap.keySet().iterator().next();
            List<Grant> grants = userListMap.get(user);
            assertNull("Метод должен вернуть исключение",userListMap);
        } catch (PersistException e) {
            throw new PersistException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginOk() throws PersistException {
        try {
            //String md5Hex = DigestUtils.md5Hex("admin");
            //System.out.println(md5Hex);
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("admin", "admin");
            User user = userListMap.keySet().iterator().next();
            List<Grant> grants = userListMap.get(user);
            assertNotNull("Метод должен вернуть обїект",userListMap);
        } catch (PersistException e) {
            throw new PersistException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }
}