package ua.glushko.services;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class UsersServiceTest {

    @Before
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
    }

    @Test
    public void testListUserAll() throws Exception {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        List<User> users = usersService.getUsersList();
        int size = users.size();
        assertTrue(size != 0);
    }

    @Test
    public void testListUserLimit() throws Exception {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        List<User> users = usersService.getUsersList(6);
        int size = users.size();
        assertTrue(size == 0);
    }

    @Test
    public void getUserById() throws SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(1);
        assertNotNull(user);
        user = usersService.getUserById(100);
        assertNull(user);
    }

    @Test
    public void getUserByLogin() throws ParameterException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserByLogin("admin");
        assertNotNull(user);
    }

    @Test
    public void getUserByLoginWrong() throws ParameterException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserByLogin("adminadmin");
        assertNull(user);
    }

    @Test
    public void updateUser() throws TransactionException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setStatus(UserStatus.BLOCKED);
        usersService.updateUser(user);
    }

    @Test
    public void updateUserWrongId() throws TransactionException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(5);
        assertNotNull(user);
        user.setId(100);
        user.setLogin("blablabla");
        usersService.updateUser(user);
    }

    @Test
    public void updateUserResetPass() throws TransactionException, ParameterException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(5);
        assertNotNull(user);
        User update = usersService.changePassword(user.getLogin(), "P@ssw0rd");
        assertNotNull(update);
    }

    @Test
    public void updateUserNullLogin() throws TransactionException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(6);
        assertNotNull(user);
        user.setLogin(null);
        usersService.updateUser(user);
        User after = usersService.getUserById(user.getId());
        assertNotEquals(after,user);
    }

    @Test
    public void deleteUser() throws TransactionException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        User user = usersService.getUserById(3);
        assertNotNull(user);
        usersService.deleteUser(3);
        User after = usersService.getUserById(3);
        assertNull(after);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        int count = usersService.count();
        usersService.deleteUser(2);
        int count2 = usersService.count();
        assertNotEquals(count2, count);
    }

    @Test
    public void getUsersAsStuff() throws TransactionException, SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        List<User> usersAsStuff = usersService.getUsersByRole(UserRole.CUSTOMER, true);
        assertTrue(usersAsStuff.size() != 0);
        for (User user : usersAsStuff) {
            usersService.deleteUser(user.getId());
        }
        usersAsStuff = usersService.getUsersByRole(UserRole.CUSTOMER, true);
        assertNull(usersAsStuff);
    }

    @Test
    public void getCount() throws SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        int count = usersService.count();
        assertTrue(count != 0);
        count = usersService.count(1);
        assertTrue(count != 0);
    }

    @Test
    public void getTitles() throws SQLException {
        UsersService usersService = UsersService.getService();
        assertNotNull(usersService);
        usersService.getUsersList();
        List<String> usersTitles = usersService.getUsersTitles();
        assertNotNull(usersTitles);
    }
}
