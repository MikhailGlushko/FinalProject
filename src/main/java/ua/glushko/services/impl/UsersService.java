package ua.glushko.services.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
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
        return  (List<User>) getList(MySQLDAOFactory.getFactory().getUserDao());
    }

    public List<User> getUsersList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<User>) getList(MySQLDAOFactory.getFactory().getUserDao(),page,pagesCount,rowsPerPage);
    }

    public List<String> getUsersTitles() {
        return MySQLDAOFactory.getFactory().getUserDao().getTableHead();
    }

    public User getUserById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getUserDao(),id);
    }

    public void updateUser(User user) throws PersistException, TransactionException {
        if(user.getLogin()==null || user.getLogin().isEmpty()|| user.getPassword()==null || user.getPassword().isEmpty())
            throw new PersistException(MessageManager.getMessage("user.incorrectLoginOrPassword"));
        update(MySQLDAOFactory.getFactory().getUserDao(),user);
    }

    public User getUserByLogin(String userLogin) throws PersistException, TransactionException {
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
        if(login==null || password==null || login.isEmpty() || password.isEmpty())
            return null;
        User user;
        Map<User, List<Grant>> userWithGrants = new HashMap<>();
        List<Grant> grants = Collections.emptyList();
        GenericDAO<User> userDAO = MySQLDAOFactory.getFactory().getUserDao();
        GenericDAO<Grant> grantDAO = MySQLDAOFactory.getFactory().getGrantDao();
        try {
            TransactionManager.beginTransaction();
            user = ((UserDAO) userDAO).checkUserAuth(login, password);
            if (Objects.nonNull(user)) {
                grants = ((GrantDAO) grantDAO).read(user.getRole().name());
                try {
                    User tmp = (User) user.clone();
                    tmp.setLastLogin(new Date(System.currentTimeMillis()));
                    userDAO.update(tmp);
                } catch (CloneNotSupportedException e) {
                    LOGGER.error(e);
                }
            }
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }

        userWithGrants.put(user, grants);
        return userWithGrants;
    }

    public User register(String login, String password, String password2, String name, String email, String phone)
            throws PersistException, TransactionException, NullPointerException {

        if (Objects.isNull(login) || Objects.isNull(password) || login.isEmpty() || password.isEmpty() || !password.equals(password2))
            throw new NullPointerException("some parameters are null");

        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        if (Objects.isNull(((UserDAO) userDao).getUserByLogin(login))) {
            User user = new User();
            try {
                TransactionManager.beginTransaction();
                user.setLogin(login);
                user.setPassword(password);
                user.setName(name);
                user.setPhone(phone);
                user.setEmail(email);
                userDao.create(user);
                TransactionManager.endTransaction();
            } finally {
                TransactionManager.rollBack();
            }
            return user;
        } else {
            throw new PersistException("user already exist");
        }
    }

    public User changePassword(String login, String password, String password2, String userSecret, String session)
            throws PersistException, TransactionException {

        if (login == null || password == null || login.isEmpty() || password.isEmpty() ||
                !password.equals(password2) || !session.equals(userSecret))
            throw new NullPointerException("some parameters are null");

        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        User user;
        try {
            user = ((UserDAO) userDao).getUserByLogin(login);
            user.setPassword(password);
            TransactionManager.beginTransaction();
            userDao.update(user);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return user;
    }

    public void deleteUser(Integer userId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getUserDao(),userId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getUserDao());
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
        List<User> result = new LinkedList<>();
        for (User item:users) {
            User tmp = new User();
            tmp.setId(item.getId());
            tmp.setName(item.getName());
            tmp.setRole(item.getRole());
            result.add(tmp);
        }
        return result;
    }
}
