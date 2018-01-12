package ua.glushko.services.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.util.*;

public class UsersService implements AbstractService {

    private UsersService() {
    }

    public static UsersService getService() {
        return new UsersService();
    }

    public List<User> getUsersList() throws PersistException, TransactionException {
        List<User> users;
        UserDAO userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            users = userDao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return users;
    }

    public List<User> getUsersList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        List<User> users;
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        try {
            TransactionManager.beginTransaction();
            users = userDao.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return users;
    }

    public List<String> getUsersTitles() {
        List<String> titles;
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        titles = userDao.getTableHead();
        return titles;
    }

    public User getUserById(int id) throws PersistException, TransactionException {
        User user;
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            user = userDao.read(id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return user;
    }

    public void updateUser(User user) throws PersistException, TransactionException {
        if(user.getLogin()==null || user.getLogin().isEmpty()|| user.getPassword()==null || user.getPassword().isEmpty())
            throw new PersistException(MessageManager.getMessage("user.incorrectLoginOrPassword"));
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            userDao.update(user);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
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
        GenericDAO<User> dao = MySQLDAOFactory.getFactory().getUserDao();
        GenericDAO<Grant> grantdao = MySQLDAOFactory.getFactory().getGrantDao();
        try {
            TransactionManager.beginTransaction();
            user = ((UserDAO) dao).checkUserAuth(login, password);
            if (Objects.nonNull(user)) {
                grants = ((GrantDAO) grantdao).read(user.getRole().name());
                try {
                    User tmp = (User) user.clone();
                    tmp.setLastLogin(new Date(System.currentTimeMillis()));
                    dao.update(tmp);
                } catch (CloneNotSupportedException e) {
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

    public User update(String login, String password, String password2, String userSecret, String session)
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
        GenericDAO<User> userDao = MySQLDAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            userDao.delete(userId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }
}
