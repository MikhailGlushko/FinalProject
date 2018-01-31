package ua.glushko.model.dao.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DAO to work with Users
 * @author Mikhail Glushko
 * @version 1.0
 */
public class UserDAO extends AbstractDAO<User> {

    private final String NAME_FIELD_ROLE = "role";
    private final String NAME_FIELD_NAME = "name";
    private final String NAME_FIELD_LOGIN = "login";
    private final String NAME_FIELD_PASSWORD = "password";
    private final String NAME_FIELD_EMAIL = "email";
    private final String NAME_FIELD_PHONE = "phone";
    private final String NAME_FIELD_STATUS = "status";
    private final String NAME_FIELD_LAST_LOGIN = "last_login";

    private static final UserDAO dao = new UserDAO();

    private UserDAO() {
        super();
    }

    public static UserDAO getInstance() {
        return dao;
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected String getFieldList() {
        return NAME_FIELD_ROLE + "," +
                NAME_FIELD_NAME + "," +
                NAME_FIELD_LOGIN + "," +
                NAME_FIELD_PASSWORD + "," +
                NAME_FIELD_EMAIL + "," +
                NAME_FIELD_PHONE + "," +
                NAME_FIELD_STATUS + "," +
                NAME_FIELD_LAST_LOGIN;
    }

    @Override
    protected void setGeneratedKey(User object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getRole().name());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getLogin());
        statement.setString(4, entity.getPassword());
        statement.setString(5, entity.getEmail());
        statement.setString(6, entity.getPhone());
        statement.setString(7, entity.getStatus().name());
        if (Objects.nonNull(entity.getLastLogin())) {
            statement.setTimestamp(8, new Timestamp(entity.getLastLogin().getTime()));
        } else
            statement.setDate(8, null);
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<User> parseResultSet(ResultSet resultSet) throws SQLException {
        List<User> list = new ArrayList<>();
        while (resultSet.next()) {
            User item = new User();
            String NAME_FIELD_ID = "id";
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setRole(resultSet.getString(NAME_FIELD_ROLE));
            item.setName(resultSet.getString(NAME_FIELD_NAME));
            item.setLogin(resultSet.getString(NAME_FIELD_LOGIN));
            item.setPassword(resultSet.getString(NAME_FIELD_PASSWORD));
            item.setEmail(resultSet.getString(NAME_FIELD_EMAIL));
            item.setPhone(resultSet.getString(NAME_FIELD_PHONE));
            item.setStatus(resultSet.getString(NAME_FIELD_STATUS));
            if (Objects.nonNull(resultSet.getTimestamp(NAME_FIELD_LAST_LOGIN)))
                item.setLastLogin(new Date(resultSet.getTimestamp(NAME_FIELD_LAST_LOGIN).getTime()));
            list.add(item);
        }
        return list;
    }

    public User checkUserAuth(String login, String pass) throws SQLException {
        String sql = getSelectQuery() +
                " where login=? and password=?";
        List<User> users;
        ResultSet resultSet;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, pass);
            resultSet = statement.executeQuery();
            users = parseResultSet(resultSet);
            if (users.size() != 1)
                return null;
        }
        return users.iterator().next();
    }

    public User getUserByLogin(String login) throws SQLException, ParameterException {
        if(login==null || login.isEmpty())
            throw new ParameterException("login is null");
        String sql = getSelectQuery() +
                " where login=?";
        List<User> users;
        ResultSet resultSet;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            users = parseResultSet(resultSet);
            if (users.size() != 1)
                return null;
        }
        return users.iterator().next();
    }

    public List<User> readByRole(UserRole  role, boolean flag) throws SQLException {
        String sql;
        if(flag)
            sql = getSelectQuery() +" where role=?";
        else
            sql = getSelectQuery() +" where role!=?";
        List<User> users;
        ResultSet resultSet;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, role.name());
            resultSet = statement.executeQuery();
            users = parseResultSet(resultSet);
            if (users.size() == 0)
                return null;
        }
        return users;
    }

    protected String getCountQuery(int userId) {
        return "select count(*) AS total from " + getTableName();
    }
}