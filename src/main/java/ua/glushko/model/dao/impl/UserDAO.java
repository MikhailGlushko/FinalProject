package ua.glushko.model.dao.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ua.glushko.model.entity.UserRole.CUSTOMER;

public class UserDAO extends AbstractDAO<User> {

    private final String NAME_TABLE = "users";

    private final String NAME_FIELD_00 = "id";
    private final String NAME_FIELD_01 = "role";
    private final String NAME_FIELD_02 = "name";
    private final String NAME_FIELD_03 = "login";
    private final String NAME_FIELD_04 = "password";
    private final String NAME_FIELD_05 = "email";
    private final String NAME_FIELD_06 = "phone";
    private final String NAME_FIELD_07 = "status";
    private final String NAME_FIELD_08 = "last_login";

    private static final UserDAO dao = new UserDAO();

    private UserDAO() {
        super();
    }

    public static UserDAO getInstance() {
        return dao;
    }

    @Override
    protected String getTableName() {
        return NAME_TABLE;
    }

    @Override
    protected String getFieldList() {
        StringBuilder builder = new StringBuilder();
        return builder
                .append(NAME_FIELD_01).append(",")
                .append(NAME_FIELD_02).append(",")
                .append(NAME_FIELD_03).append(",")
                .append(NAME_FIELD_04).append(",")
                .append(NAME_FIELD_05).append(",")
                .append(NAME_FIELD_06).append(",")
                .append(NAME_FIELD_07).append(",")
                .append(NAME_FIELD_08).toString();
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
            item.setId(resultSet.getInt(NAME_FIELD_00));
            item.setRole(resultSet.getString(NAME_FIELD_01));
            item.setName(resultSet.getString(NAME_FIELD_02));
            item.setLogin(resultSet.getString(NAME_FIELD_03));
            item.setPassword(resultSet.getString(NAME_FIELD_04));
            item.setEmail(resultSet.getString(NAME_FIELD_05));
            item.setPhone(resultSet.getString(NAME_FIELD_06));
            item.setStatus(resultSet.getString(NAME_FIELD_07));
            if (Objects.nonNull(resultSet.getTimestamp(NAME_FIELD_08)))
                item.setLastLogin(new Date(resultSet.getTimestamp(NAME_FIELD_08).getTime()));
            list.add(item);
        }
        return list;
    }

    public User checkUserAuth(String login, String pass) throws PersistException {
        String sql = getSelectQuery() +
                " where login=? and password=?";
        List<User> users = Collections.emptyList();
        ResultSet resultSet = null;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, pass);
            resultSet = statement.executeQuery();
            users = parseResultSet(resultSet);
            String PROPERTY_NAME_USER_INCORRECT_LOGIN_OR_PASSWORD = "user.incorrectLoginOrPassword";
            if (users.size() == 0)
                throw new PersistException(MessageManager.getMessage(PROPERTY_NAME_USER_INCORRECT_LOGIN_OR_PASSWORD));
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return users.iterator().next();
    }

    public User getUserByLogin(String login) throws PersistException, NullPointerException {
        if(login==null || login.isEmpty())
            throw new NullPointerException("login is null");
        String sql = getSelectQuery() +
                " where login=?";
        List<User> users = Collections.emptyList();
        ResultSet resultSet = null;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            users = parseResultSet(resultSet);
            if (users.size() == 0)
                return null;
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return users.iterator().next();
    }

    public List<User> readStuff(UserRole  role, boolean flag) throws PersistException {
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
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return users;
    }
}