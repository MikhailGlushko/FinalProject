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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

public class LoginServiceTest {

    private UsersService loginService;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        loginService = UsersService.getService();
        assertNotNull(loginService);
    }

    @Test
    public void loginWrong() throws SQLException {
        try {
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("misha", "admin");
            User user = null;
            if(Objects.nonNull(userListMap))
                user = userListMap.keySet().iterator().next();
            List<Grant> grants = null;
            if(Objects.nonNull(user))
                grants = userListMap.get(user);
            assertNull(grants);
            assertNull("Метод должен вернуть исключение",userListMap);
        } catch (DaoException e) {
            throw new DaoException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginOk() throws SQLException {
        try {
            Map<User, List<Grant>> userListMap = loginService.authenticateUser("admin", "admin");
            User user = null;
            if(Objects.nonNull(userListMap))
                user = userListMap.keySet().iterator().next();
            List<Grant> grants = null;
            if(Objects.nonNull(user))
                grants = userListMap.get(user);
            assertNull(grants);
        } catch (DaoException e) {
            throw new DaoException(e);
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }
}