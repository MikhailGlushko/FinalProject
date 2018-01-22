package ua.glushko.services;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.model.exception.DaoException;
import ua.glushko.model.exception.DatabaseException;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
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
    }

    @Test
    public void getUserById() throws DaoException, TransactionException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(1);
        assertNotNull(user);
        user=usersService.getUserById(100);
        assertNull(user);
    }

    @Test
    public void getUserByLogin() throws DaoException, TransactionException, ParameterException, DatabaseException {

            UsersService usersService = UsersService.getService();
            User user = usersService.getUserByLogin("admin");
            assertNotNull(user);
    }

    @Test (expected = DaoException.class)
    public void getUserByLoginWrong() throws DaoException, TransactionException, ParameterException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserByLogin("adminadmin");
        assertNull(user);
    }

    @Test
    public void updateUser() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setStatus(UserStatus.BLOCKED);
        usersService.updateUser(user);
    }

    @Test(expected = DaoException.class)
    public void updateUserWrongId() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setId(100);
        user.setLogin("blablabla");
        usersService.updateUser(user);
    }

    @Test
    public void updateUserResetPass() throws DaoException, TransactionException, ParameterException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(5);
        assertNotNull(user);
        User update = usersService.changePassword(user.getLogin(), "P@ssw0rd");
    }

    @Test
    public void updateUserNullLogin() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(6);
        assertNotNull(user);
        user.setLogin(null);
        usersService.updateUser(user);
    }

    @Test
    public void deleteUser() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(3);
        assertNotNull(user);
        usersService.deleteUser(3);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        UsersService usersService = UsersService.getService();
        int count = usersService.count();
        usersService.deleteUser(2);
        int count2 = usersService.count();
        assertNotEquals(count2,count);
    }

    @Test
    public void getUsersAsStuff() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        List<User> usersAsStuff = usersService.getUsersAsStuff(UserRole.CUSTOMER, true);
        assertTrue(usersAsStuff.size()!=0);
        for (User user: usersAsStuff) {
            usersService.deleteUser(user.getId());
        }
        usersAsStuff = usersService.getUsersAsStuff(UserRole.CUSTOMER, true);
        assertNull(usersAsStuff);
    }

    @Test
    public void getCount() throws SQLException, TransactionException {
        UsersService usersService = UsersService.getService();
        int count = usersService.count();
        assertTrue(count!=0);
        count = usersService.count(1);
        assertTrue(count!=0);
    }

    @Test
    public void getTitles() throws DaoException, TransactionException, DatabaseException {
        UsersService usersService = UsersService.getService();
        usersService.getUsersList();
        List<String> usersTitles = usersService.getUsersTitles();
        assertNotNull(usersTitles);
    }
}
