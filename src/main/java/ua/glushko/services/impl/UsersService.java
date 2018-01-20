package ua.glushko.services.impl;

import org.apache.commons.codec.digest.DigestUtils;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.util.*;

public class UsersService extends AbstractService {

    private UsersService() {
    }

    public static UsersService getService() {
        return new UsersService();
    }

    public List<User> getUsersList() throws PersistException, TransactionException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        List<User> read;
        try {
            TransactionManager.beginTransaction();
            read = userDao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<User> getUsersList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<User> read;
        try {
            TransactionManager.beginTransaction();
            read = userDao.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getUsersTitles() {
        return MySQLDAOFactory.getFactory().getUserDao().getTableHead();
    }

    public User getUserById(int id) throws PersistException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        return userDao.read(id);
    }

    public void updateUser(User user) throws PersistException, TransactionException {
        //if (user.getLogin() == null || user.getLogin().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty())
        //    throw new PersistException(MessageManager.getMessage("user.incorrectLoginOrPassword"));
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            if (user.getId() != null && user.getId() != 0) {
                User oldUser = userDao.read(user.getId());
                if(Objects.isNull(oldUser))
                    throw new PersistException("user.not.found");
                oldUser.setName(user.getName());
                oldUser.setLogin(user.getLogin());
                oldUser.setPhone(user.getPhone());
                oldUser.setEmail(user.getEmail());
                if(user.getPassword()!=null && user.getPassword()!="")
                    oldUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
                userDao.update(oldUser);
            }
            else
                userDao.create(user);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    public User getUserByLogin(String userLogin) throws PersistException, TransactionException, ParameterException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        User user;
        try {
            TransactionManager.beginTransaction();
            user = ((UserDAO) userDao).getUserByLogin(userLogin);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return user;
    }

    public Map<User, List<Grant>> authenticateUser(String login, String password) throws TransactionException, PersistException {
        User user;
        Map<User, List<Grant>> userWithGrants = new HashMap<>();
        List<Grant> grants = Collections.emptyList();
        GenericDAO<User> userDAO = MySQLDAOFactory.getFactory().getUserDao();
        GenericDAO<Grant> grantDAO = MySQLDAOFactory.getFactory().getGrantDao();
        try {
            String m5HexPassword = DigestUtils.md5Hex(password);
            TransactionManager.beginTransaction();
            user = ((UserDAO) userDAO).checkUserAuth(login, m5HexPassword);
            if (Objects.nonNull(user)) {
                grants = ((GrantDAO) grantDAO).read(user.getRole().name());
                User tmp = (User) user.clone();
                tmp.setLastLogin(new Date(System.currentTimeMillis()));
                userDAO.update(tmp);
            }
            TransactionManager.endTransaction();
        } finally {
            LOGGER.info("ROLLBACK");
            TransactionManager.rollBack();
        }

        userWithGrants.put(user, grants);
        return userWithGrants;
    }

    public User register(String login, String password, String name, String email, String phone)
            throws PersistException, TransactionException, ParameterException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        User userByLogin = null;
        try {
            userByLogin = ((UserDAO) userDao).getUserByLogin(login);
        } catch (PersistException e) {
            // user not found. It's OK. Let's go to register it.
        }
        if (Objects.isNull(userByLogin)) {
            User user = new User();
            try {
                String m5HexPassword = DigestUtils.md5Hex(password);
                user.setPassword(m5HexPassword);
                TransactionManager.beginTransaction();
                user.setLogin(login);
                user.setPassword(m5HexPassword);
                user.setName(name);
                user.setPhone(phone);
                user.setEmail(email);
                userDao.create(user);
                TransactionManager.endTransaction();
                LOGGER.debug("New user registered");
            } finally {
                TransactionManager.rollBack();
            }
            return user;
        } else {
            throw new PersistException("user already exist");
        }
    }

    public User changePassword(String login, String password)
            throws PersistException, TransactionException, ParameterException {

        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        User user;
        try {
            String m5HexPassword = DigestUtils.md5Hex(password);
            user = ((UserDAO) userDao).getUserByLogin(login);
            user.setPassword(m5HexPassword);
            TransactionManager.beginTransaction();
            userDao.update(user);
            TransactionManager.endTransaction();
            LOGGER.debug("Password changed");
        } finally {
            TransactionManager.rollBack();
        }
        return user;
    }

    public void deleteUser(Integer userId) throws PersistException, TransactionException {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            userDao.delete(userId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    public int count() {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        return userDao.count();
    }

    public int count(int id) {
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        return ((UserDAO) userDao).count(id);
    }

    public List<User> getUsersAsStuff(UserRole role, boolean flag) throws PersistException, TransactionException {
        List<User> users;
        try {
            GenericDAO<User> userDAO = MySQLDAOFactory.getFactory().getUserDao();
            TransactionManager.beginTransaction();
            users = ((UserDAO) userDAO).readStuff(role, flag);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        List<User> result = null;
        if (users != null) {
            result = new LinkedList<>();
            for (User item : users) {
                User tmp = new User();
                tmp.setId(item.getId());
                tmp.setName(item.getName());
                tmp.setRole(item.getRole());
                result.add(tmp);
            }
        }
        return result;
    }
}
