package ua.glushko.services;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.*;

public class UsersServiceTest {

    Logger logger = Logger.getLogger(UsersServiceTest.class.getSimpleName());

    @Before
    public void getService() {
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
    }

    @Test
    public void testListUserAll() throws Exception {
        UsersService usersService = UsersService.getService();
        List<User> users = usersService.getUsersList();
        int size = users.size();
        assertTrue(size!=0);
    }

    @Test
    public void testListUserLimit() throws Exception {
        UsersService usersService = UsersService.getService();
        List<User> users = usersService.getUsersList(6,5,5);
        int size = users.size();
        assertTrue(size!=0);
    }

    @Test
    public void gerUserById() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(1);
        assertNotNull(user);
        user=usersService.getUserById(100);
        assertNull(user);
    }

    @Test
    public void getUserByLogin() throws PersistException, TransactionException {

            UsersService usersService = UsersService.getService();
            User user = usersService.getUserByLogin("admin");
            assertNotNull(user);
    }

    @Test
    public void getUserByLoginWrong() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserByLogin("adminadmin");
        assertNull(user);
    }

    @Test
    public void updateUser() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setStatus(UserStatus.BLOCKED);
        usersService.updateUser(user);
    }

    @Test(expected = PersistException.class)
    public void updateUserWrongId() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        Integer id = user.getId();
        user.setId(0);
        usersService.updateUser(user);
    }

    @Test
    public void updateUserResetPass() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        User update = usersService.changePassword(user.getLogin(), "pass1", "pass1", "", "");
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWrongPass() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        User update = usersService.changePassword(user.getLogin(), "pass1", "pass2", "", "");
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWrongKey() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        User update = usersService.changePassword(user.getLogin(), "pass1", "pass1", "1", "2");
    }

    @Test(expected = PersistException.class)
    public void updateUserNullLogin() throws PersistException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setLogin(null);
        usersService.updateUser(user);
    }
}
