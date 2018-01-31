package ua.glushko.services.impl;

import org.apache.commons.codec.digest.DigestUtils;
import ua.glushko.configaration.ConfigurationManager;
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

import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_PAGES_COUNT;
import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_ROWS_COUNT;

/**
 * User services
 * @author Mikhail Glushko
 * @version 1.0
 */
public class UsersService extends Service {

    private UsersService() {
    }

    public static UsersService getService() {
        return new UsersService();
    }

    /** List of Users */
    public List<User> getUsersList() throws SQLException {
        return DAOFactory.getFactory().getUserDao().read();
    }

    /** List of Users with limit */
    public List<User> getUsersList(int page) throws SQLException {
        Integer pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        Integer rowsPerPage = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        return DAOFactory.getFactory().getUserDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names */
    public List<String> getUsersTitles() {
        return DAOFactory.getFactory().getUserDao().getTableHead();
    }

    /** Get User by id */
    public User getUserById(int id) throws SQLException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        return userDao.read(id);
    }

    /** Update exist user or create new */
    public void updateUser(User user) throws TransactionException, SQLException {
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
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
    }

    /** Get user by login */
    public User getUserByLogin(String userLogin) throws ParameterException, SQLException {
        return DAOFactory.getFactory().getUserDao().getUserByLogin(userLogin);
    }

    /** Get user Grants */
    public Map<User, List<Grant>> authenticateUser(String login, String password) throws TransactionException, SQLException {
        Map<User, List<Grant>> userWithGrants = null;
        GenericDAO<User> userDAO = DAOFactory.getFactory().getUserDao();
        GenericDAO<Grant> grantDAO = DAOFactory.getFactory().getGrantDao();
        try {
            String m5HexPassword = DigestUtils.md5Hex(login+password);
            TransactionManager.beginTransaction();
            User user = ((UserDAO) userDAO).checkUserAuth(login, m5HexPassword);
            if (Objects.nonNull(user)) {
                List<Grant> grants = ((GrantDAO) grantDAO).read(user.getRole().name());
                User tmp = (User) user.clone();
                tmp.setLastLogin(new Date(System.currentTimeMillis()));
                userDAO.update(tmp);
                userWithGrants = new HashMap<>();
                userWithGrants.put(user, grants);
            }
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
        return userWithGrants;
    }

    /** Register new User */
    public User register(String login, String password, String name, String email, String phone)
            throws TransactionException, ParameterException, SQLException {
        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        User userByLogin = ((UserDAO) userDao).getUserByLogin(login);
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
            } catch (SQLException e){
                TransactionManager.rollBack();
            }
            return user;
        } else {
            return null;
        }
    }

    /** Change password */
    public User changePassword(String login, String password)
            throws TransactionException, ParameterException, SQLException {

        GenericDAO<User> userDao = DAOFactory.getFactory().getUserDao();
        User user = null;
        try {
            String m5HexPassword = DigestUtils.md5Hex(login+password);
            TransactionManager.beginTransaction();
            user = ((UserDAO) userDao).getUserByLogin(login);
            if(Objects.nonNull(user)) {
                user.setPassword(m5HexPassword);
                userDao.update(user);
            }
            TransactionManager.endTransaction();
            LOGGER.debug("Password changed");
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
        return user;
    }

    /** delete exist User */
    public void deleteUser(Integer userId) throws TransactionException, SQLException {
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
    public List<User> getUsersByRole(UserRole role, boolean noInvertRole) throws SQLException {
        return (DAOFactory.getFactory().getUserDao()).readByRole(role,noInvertRole);
    }
}
