package ua.glushko.model.dao;

import ua.glushko.exception.DatabaseException;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.exception.DaoException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * CRUD Implementation for DAO (Template method)
 * @version 1.0
 * @author Mikhail Glushko
 * @param <T>
 */
abstract public class AbstractDAO<T extends GenericEntity> implements GenericDAO<T> {

    private List<String> titles;
    protected AbstractDAO() {
        super();
    }

    /**
     * Create new GenericEntity
     * @param object - Entity
     * @throws DaoException - Exception
     */
    @Override
    public void create(T object) throws SQLException {
        String sql = getCreateQuery();
        ResultSet generatedKeys;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForCreate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows == 0) {
                throw new DaoException("Can not insert record to database. Operation aborted.");
            }
            generatedKeys = statement.getGeneratedKeys();
            setGeneratedKey(object, generatedKeys);
        }
    }

    /**
     * Get generated key after create
     */
    protected abstract void setGeneratedKey(T object, ResultSet generatedKeys) throws SQLException;

    /**
     * setup prepare statement for creation
     */
    protected abstract void prepareStatementForCreate(PreparedStatement statement, T object) throws SQLException;

    /**
     * query string for create
     */
    private String getCreateQuery() {
        return "insert into " + getTableName() +
                " (" + getFieldList() +
                ") values " + getFieldListForInsert(getFieldList());
    }

    /**
     * Update exist GenericEntity
     */
    @Override
    public void update(T object) throws SQLException {
        String sql = getUpdateQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows != 1) {
                throw new DaoException("Can not update record - record not found. Unsuccessful operation.");
            }
        }
    }

    /**
     * setup prepare statement for update
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws SQLException;

    /**
     * get the text of the update request
     */
    private String getUpdateQuery() {
        return "update " + getTableName() +
                " set " + getFieldListForUpdate(getFieldList()) +
                " where id=?";
    }

    /**
     * delete exist GenericEntity
     */

    public T delete(int id) throws SQLException {
        T object = read(id);
        String sql = getDeleteQuery() + " where id=?";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForDeleteByKey(statement, id);
            int effectedRows = statement.executeUpdate();
            if (effectedRows != 1) {
                throw new DaoException("Can not delete record. Operation aborted");
            }
        }
        return object;
    }

    /**
     * setup prepare statement for delete
     */
    private void prepareStatementForDeleteByKey(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * Delete all exist GenericEntities
     */
    @Override
    public void deleteAll() throws SQLException {
        String sql = getDeleteQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            int effectedRows = statement.executeUpdate();
            if (effectedRows == 0) {
                throw new DaoException("Can not delete record. Operation aborted");
            }
        }
    }

    /**
     * query string for delete
     */
    private String getDeleteQuery() {
        return "delete from " + getTableName();
    }

    /**
     * Read GenericEntity from database by id
     */
    public T read(int id) throws SQLException {
        List<T> list;
        String sql = getSelectQueryById();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForSelectById(statement, id);
            ResultSet resultSet = statement.executeQuery();
            list = parseResultSet(resultSet);
        }
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new DaoException("Received more than one record.");
        }
        return list.iterator().next();
    }

    /**
     * Populate result to list
     */
    protected abstract List<T> parseResultSet(ResultSet resultSet) throws SQLException;

    /**
     * Prepare statement for select
     */
    private void prepareStatementForSelectById(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * Get list of GenericEntities by name
     */
    public List<T> read(String name) throws SQLException {
        List<T> list;
        String sql = getSelectQuery() +
                " where name=?";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForSelectByName(statement, name);
            ResultSet resultSet = statement.executeQuery();
            setTitles(statement.getMetaData());
            list = parseResultSet(resultSet);
        }
        return list;
    }

    /**
     * Prepare statement for select
     */
    protected void prepareStatementForSelectByName(PreparedStatement statement, String name) throws SQLException {
        statement.setString(1, name);
    }

    /**
     * Get list of all GenericEntities
     */
    public List<T> read() throws SQLException {
        List<T> list;
        String sql = getSelectQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            setTitles(statement.getMetaData());
            list = parseResultSet(resultSet);
        }
        return list;
    }

    /**
     * Get list of limited GenericEntities
     * @param start - start
     * @param limit - limit
     * @return list of Entities
     * @throws DaoException - exception
     */
    public List<T> read(int start, int limit) throws SQLException {
        List<T> list;
        String sql = getSelectQueryWithLimit();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, start);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        }
        return list;
    }

    /**
     * query for select
     */
    protected String getSelectQuery() {
        return "select id, " + getFieldList() +
                " from " + getTableName();
    }

    protected String getSelectQueryById() {
        return "select id, " + getFieldList() +
                " from " + getTableName() +
                " where id =?";
    }

    protected String getSelectQueryWithLimit() {
        return "select id, " + getFieldList() +
                " from " + getTableName() +
                " limit ?,? ";
    }

    protected abstract String getFieldList();

    protected abstract String getTableName();

    /**
     * Prepare field list for insert
     */
    private String getFieldListForInsert(String fieldList) {
        String[] fields = fieldList.split(",");
        int count = fields.length;
        StringBuilder sb = new StringBuilder("( ");
        for (int i = 0; i < count - 1; i++)
            sb.append("?,");
        if (count > 0)
            sb.append("? )");
        return sb.toString();
    }

    /**
     * Prepare field list for update
     */
    private String getFieldListForUpdate(String fieldList) {
        return fieldList.replaceAll(",", "=?,") + "=?";
    }

    public List<String> getTableHead() {
        return titles;
    }

    protected void setTitles(ResultSetMetaData metaData) throws SQLException {
        List<String> titles = new LinkedList<>();
        int columnCount = metaData.getColumnCount();
        for (int i=1; i<=columnCount; i++){
            titles.add(metaData.getColumnName(i));
        }
        this.titles = titles;
    }

    public Integer count() throws SQLException {
        String sql = getCountQuery();
        try(ConnectionWrapper con = TransactionManager.getConnection();
        PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("total");
        }
        return null;
    }

    public Integer count(int userId) throws SQLException {
        String sql = getCountQuery(userId);
        try(ConnectionWrapper con = TransactionManager.getConnection();
            PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("total");
        }
        return null;
    }

    protected String getCountQuery() {
        return "select count(*) AS total from " + getTableName();
    }

    abstract protected String getCountQuery(int userId);
}

