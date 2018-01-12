package ua.glushko.model.dao;

import org.apache.log4j.Logger;
import org.junit.*;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static ua.glushko.model.dao.H2DataSource.*;

public class UserDAOTest {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getSimpleName());

    /**
     * если хотим видеть ошибки на русском языке - убираем коентарий со статичного блока
     */
    /*static{
          Locale.setDefault(new Locale("RU"));
    }*/

    private static GenericDAO<User> userDAO;

    @Before
    public void init() {
            ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
    }

    @Test
         public void getUsers() {
        try {
            assertNotNull(userDAO);
            //получаем список пользователей, должен быть не пустой
            List<User> list = userDAO.read();
            assertTrue("Таблица пользователей пустая", list.size() != 0);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void getUserswitLimit() {
        try {
            assertNotNull(userDAO);
            //получаем список пользователей, должен быть не пустой
            List<User> list = userDAO.read(0,25);
            assertTrue("Таблица пользователей пустая", list.size() == 25);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void getExistUserById() {
        try {
            User user = userDAO.read(1);
            assertNotNull("Пользователь с ключом 1 не найден", user);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void getNoExistUserById() {
        try {
            // получаем пользователя с ключом 10, нет такого
            User user = userDAO.read(100);
            assertNull("Пользователя с ключом 10 не должно быть", user);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void getExistUserByName() {
        try {
            // получаем пользователя с существующим именем
            List<User> list = ((UserDAO) userDAO).read("Manager");
            assertTrue("Пользователь с именем Manager не найден", list.size() != 0);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void getNoExistUserByName() {
        try {
            // получаем пользователя с несуществующим именем
            List<User> list = ((UserDAO) userDAO).read("MANAGER1");
            assertTrue("Пользователя с именем MANAGER1 не должно быть", list.size() == 0);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    @Test
    public void createNewUserWithoutCommit() throws TransactionException, SQLException {
        try {
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            assertNotNull(userDAO);
            //создаем нового пользователя
            TransactionManager.beginTransaction();
            User read = userDAO.read(5);
            logger.info("before insert: " + read);
            User user = new User();
            user.setName("Test User");
            user.setLogin("Test.Login");
            user.setPassword("P@ssw0rd");
            user.setEmail("email@email.com");
            user.setPhone("123456789");
            //user.setRole(UserRole.ADMIN);
            //user.setStatus(UserStatus.BLOCK);
            logger.info("new instance: " + user);
            userDAO.create(user);
            read = userDAO.read(user.getId());
            logger.info("after insert: " + user);
            assertTrue("Пользоветель не добавлен в базу данных", user.getId() != 0);
            TransactionManager.rollBack();
            read = userDAO.read(5);
            logger.info("after rollback: " + read);
            //добавляем пользователя в базу данных, должен измениться id
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            TransactionManager.rollBack();
            logger.error(e);
        }
    }

    @Test
    public void createNewUserWithCommit() throws TransactionException, SQLException {
        try {
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            assertNotNull(userDAO);
            //создаем нового пользователя
            TransactionManager.beginTransaction();
            User read = userDAO.read(5);
            logger.info("before insert: " + read);
            User user = new User();
            user.setName("Test User");
            user.setLogin("Test.Login");
            user.setPassword("P@ssw0rd");
            user.setEmail("email@email.com");
            user.setPhone("123456789");
            //user.setRole(UserRole.ADMIN);
            //user.setStatus(UserStatus.BLOCK);
            logger.info("new instance: " + user);
            userDAO.create(user);
            read = userDAO.read(user.getId());
            logger.info("after insert: " + user);
            assertTrue("Пользоветель не добавлен в базу данных", user.getId() != 0);
            TransactionManager.endTransaction();
        } catch (TransactionException e) {
            TransactionManager.rollBack();
            logger.error(e);
        } catch (PersistException e) {
            logger.error(e);
        }
        User read = userDAO.read(5);
        logger.info("after commit: " + read);
        //добавляем пользователя в базу данных, должен измениться id
    }


    @Test
    public void updateExistUser() {
        try {
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            TransactionManager.beginTransaction();
            User u1 = userDAO.read(1);
            u1.setName(u1.getName() + u1.getId());
            u1.setLastLogin(new Date(System.currentTimeMillis()));
            // вносим изменения по существующему пользователю, это уже разные сущности
            userDAO.update(u1);
            User u2 = userDAO.read(1);
            logger.info(u2);
            assertNotSame("Пользователи должны отличаться", u2, u1);
            TransactionManager.rollBack();
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            logger.error(e);
        }
    }

    @Test(expected = PersistException.class)
    public void updateNoExistUser() throws PersistException {
        try {
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            User u1 = userDAO.read(1);
            u1.setName(u1.getName() + u1.getId());
            u1.setId(10);
            // пробуем внести изменения по не существующему пользователю, ожидаем исключение
            userDAO.update(u1);
        } catch (PersistException e) {
            logger.error(e);
            throw e;
        }
    }


    @Test
    public void deleteOneExist() {
        try {
            TransactionManager.beginTransaction();
            User read1 = userDAO.read(1);
            assertNotNull(read1);
            // удаляем существующую запись
            userDAO.delete(1);
            // проверяем наличие удаленной записи в базе данных, должен вернуться null
            User read2 = userDAO.read(1);
            assertNull("Пользователя с ключом не должно быть", read2);
            TransactionManager.rollBack();
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            logger.error(e);
        }
    }

    @Test(expected = PersistException.class)
    public void deleteOneNoExist() throws PersistException {
        try {
            // пробуем удалить не существующую запись, ожидаем исключение
            userDAO.delete(100);
        } catch (PersistException e) {
            logger.error(e);
            throw e;
        }
    }

    @Test
    public void deleteAll() {
        try {
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            TransactionManager.beginTransaction();
            List<User> list = userDAO.read();
            assertNotNull(list);
            // удаляем всех пользователей
            userDAO.deleteAll();
            // ожидаем пустой список
            List<User> list2 = userDAO.read();
            assertEquals("Не должно быть пользователей", 0, list2.size());
            TransactionManager.rollBack();
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            logger.error(e);
        }
    }

    @Test
    public void deleteOneRollBack() {
        try {
            TransactionManager.beginTransaction();
            userDAO = MySQLDAOFactory.getFactory().getUserDao();
            List<User> read1 = userDAO.read();
            // удаляем пользователя с ключом 1
            User u = userDAO.delete(1);
            // подсчитывем количество пользователей, ожидается на 1 меньше
            List<User> read2 = userDAO.read();
            assertTrue("Пользователь не біл удален", read1.size() != read2.size());
            // добавляем удаленного пользователя
            userDAO.create(u);
            // подсчитываем количество пользователей, ожидаем на 1 больше
            List<User> read4 = userDAO.read();
            // аннулируем операции
            TransactionManager.rollBack();
            //подсчитывем количество пользователей, ожидаем старое значение
            userDAO.read();
            assertTrue("Операции не біли отменені", read1.size() == read4.size());
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            logger.error(e);
        }
    }

    @Test
    public void deleteOneAndreadItInOtherThread() {
        try {
            ConnectionPool.getConnectionPool().setDataSource(H2_CONNECTION_POOL);
            TransactionManager.beginTransaction();
            List<User> read1 = userDAO.read();
            // откріваем транзакцию
            userDAO.delete(1);
            // пробуем прочитать данные до завершения транзакйии
            List<User> read2 = userDAO.read();
            assertEquals(read1.size() - 1, read2.size());
            logger.info("threat 1 "+read2.size());
            // создаем вторую копию ДАО в другом потоке
            new Thread() {
                @Override
                public void run() {
                    // и пробуем прочитать те же данные
                    try {
                        Thread.sleep(100);
                        GenericDAO<User> userDAO = MySQLDAOFactory.getFactory().getUserDao();
                        List<User> read3 = userDAO.read();
                        logger.info("thread 2 "+read3.size());
                        Thread.sleep(100);
                    } catch (PersistException e) {
                        logger.error(e);
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
                }
            }.start();
            Thread.sleep(300);
            //assertEquals(read1.size(), read3.size());
            TransactionManager.rollBack();
        } catch (SQLException e) {
            logger.error(e);
        } catch (TransactionException e) {
            logger.error(e);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }
}