package ua.glushko.services.impl;

import org.apache.commons.codec.digest.DigestUtils;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.*;

public class UsersService extends Service {

    private UsersService() {
    }

    public static UsersService getService() {
        return new UsersService();
    }

    /** List of Users */
    public List<User> getUsersList() throws TransactionException, DatabaseException {
        return DAOFactory.getFactory().getUserDao().read();
    }

    /** List of Users with limit */
    public List<User> getUsersList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getUserDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names */
    public List<String> getUsersTitles() {
        return DAOFactory.getFactory().getUserDao().getTableHead();
    }

    /** Get User by id */
    public User getUserById(int id) throws DaoException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        return userDao.read(id);
    }

    /** Update exist user or create new */
    public void updateUser(User user) throws TransactionException, DatabaseException {
        //if (user.getLogin() == null || user.getLogin().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty())
        //    throw new DaoException(MessageManager.getMessage("user.incorrectLoginOrPassword"));
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            if (user.getId() != null && user.getId() != 0) {
                User oldUser = userDao.read(user.getId());
                if(Objects.isNull(oldUser))
                    throw new DaoException("user.not.found");
                oldUser.setName(user.getName());
                oldUser.setLogin(user.getLogin());
                oldUser.setPhone(user.getPhone());
                oldUser.setEmail(user.getEmail());
                if(user.getPassword()!=null && !Objects.equals(user.getPassword(), ""))
                    oldUser.setPassword(DigestUtils.md5Hex(user.getLogin()+user.getPassword()));
                oldUser.setStatus(user.getStatus());
                oldUser.setRole(user.getRole());
                userDao.update(oldUser);
            }
            else
                userDao.create(user);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    /** Get user by login */
    public User getUserByLogin(String userLogin) throws TransactionException, ParameterException, DatabaseException {
        return DAOFactory.getFactory().getUserDao().getUserByLogin(userLogin);
    }

    /** Get user Grants */
    public Map<User, List<Grant>> authenticateUser(String login, String password) throws TransactionException, DatabaseException {
        User user;
        Map<User, List<Grant>> userWithGrants = new HashMap<>();
        List<Grant> grants = Collections.emptyList();
        GenericDAO<User> userDAO = DAOFactory.getFactory().getUserDao();
        GenericDAO<Grant> grantDAO = DAOFactory.getFactory().getGrantDao();
        try {
            String m5HexPassword = DigestUtils.md5Hex(login+password);
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

    /** Register new User */
    public User register(String login, String password, String name, String email, String phone)
            throws TransactionException, ParameterException, DatabaseException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        User userByLogin = null;
        try {
            userByLogin = ((UserDAO) userDao).getUserByLogin(login);
        } catch (DaoException e) {
            // user not found. It's OK. Let's go to register it.
        }
        if (Objects.isNull(userByLogin)) {
            User user = new User();
            try {
                String m5HexPassword = DigestUtils.md5Hex(login+password);
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
            throw new DaoException("user already exist");
        }
    }

    /** Change password */
    public User changePassword(String login, String password)
            throws TransactionException, ParameterException, DatabaseException {

        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        User user;
        try {
            String m5HexPassword = DigestUtils.md5Hex(login+password);
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

    /** delete exist User */
    public void deleteUser(Integer userId) throws TransactionException, DatabaseException {
        delete(DAOFactory.getFactory().getUserDao(),userId);
    }

    /** Total of Users */
    public int count() throws SQLException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        return userDao.count();
    }

    public int count(int id) throws SQLException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        return ((UserDAO) userDao).count(id);
    }

    /** List of Stuff Users */
    public List<User> getUsersByRole(UserRole role, boolean noInvertRole) throws TransactionException, DatabaseException {
        return (DAOFactory.getFactory().getUserDao()).readByRole(role,noInvertRole);
    }
}
