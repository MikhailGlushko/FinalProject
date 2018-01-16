package ua.glushko.model.dao;

import org.apache.log4j.Logger;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

abstract public class AbstractDAO<T extends GenericEntity> implements GenericDAO<T> {
    protected static final Logger logger = Logger.getLogger(AbstractDAO.class.getSimpleName());

    private List<String> titles;
    protected AbstractDAO() {
        super();
    }

    /**
     * Create new entity
     * @param object
     * @throws PersistException
     */
    @Override
    public void create(T object) throws PersistException {
        String sql = getCreateQuery();
        ResultSet generatedKeys = null;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForCreate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows == 0) {
                throw new PersistException("Can not insert record to database. Operation aborted.");
            }
            generatedKeys = statement.getGeneratedKeys();
            setGeneratedKey(object, generatedKeys);
        } catch (SQLException e) {
            throw new PersistException(e);
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
     * Update exist entity
     */
    @Override
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows != 1) {
                throw new PersistException("Can not update record - record not found. Unsuccessful operation.");
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    /**
     * setup prepare statement for update
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws SQLException;

    /**
     * получаем текст запроса на обновление
     */
    private String getUpdateQuery() {
        return "update " + getTableName() +
                " set " + getFieldListForUpdate(getFieldList()) +
                " where id=?";
    }

    /**
     * delete exist entity
     */

    public T delete(int id) throws PersistException {
        T object = read(id);
        String sql = getDeleteQuery() + " where id=?";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForDeleteByKey(statement, id);
            int effectedRows = statement.executeUpdate();
            if (effectedRows != 1) {
                throw new PersistException("Can not delete record. Operation aborted");
            }
        } catch (SQLException e) {
            throw new PersistException(e);
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
     * Delete exist entity
     */
    @Override
    public void deleteAll() throws PersistException {
        String sql = getDeleteQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            int effectedRows = statement.executeUpdate();
            if (effectedRows == 0) {
                throw new PersistException("Can not delete record. Operation aborted");
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    /**
     * query string for delete
     */
    private String getDeleteQuery() {
        return "delete from " + getTableName();
    }

    /**
     * Read entity from database by id
     */
    public T read(int id) throws PersistException {
        List<T> list = Collections.emptyList();
        String sql = getSelectQuery() +
                " where id=?";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForSelectById(statement, id);
            ResultSet resultSet = statement.executeQuery();
            list = parseResultSet(resultSet);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    /**
     * Polulate result to list
     */
    protected abstract List<T> parseResultSet(ResultSet resultSet) throws SQLException;

    /**
     * Prepare statement for select
     */
    protected void prepareStatementForSelectById(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * Get list by name
     */
    public List<T> read(String name) throws PersistException {
        List<T> list = Collections.emptyList();
        String sql = getSelectQuery() +
                " where name=?";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForSelectByName(statement, name);
            ResultSet resultSet = statement.executeQuery();
            setTitles(statement.getMetaData());
            list = parseResultSet(resultSet);
        } catch (Exception e) {
            throw new PersistException(e);
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
     * Get list of all entities
     */
    public List<T> read() throws PersistException {
        List<T> list = Collections.emptyList();
        String sql = getSelectQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            setTitles(statement.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return list;
    }

    /**
     * Get list of limited entities
     * @param start
     * @param limit
     * @return
     * @throws PersistException
     */
    public List<T> read(int start, int limit) throws PersistException {
        List<T> list = Collections.emptyList();
        String sql = getSelectQuery(start, limit);
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, start);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new PersistException(e);
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

    private String getSelectQuery(int from, int limit) {
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

    @Override
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

    public Integer count(){
        String sql = getCountSqury();
        try(ConnectionWrapper con = TransactionManager.getConnection();
        PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("total");
        } catch (Exception e){}
        return null;
    }

    public Integer count(int userId){
        String sql = getCountSqury(userId);
        try(ConnectionWrapper con = TransactionManager.getConnection();
            PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("total");
        } catch (Exception e){}
        return null;
    }

    protected String getCountSqury() {
        return "select count(*) AS total from " + getTableName();
    }

    abstract protected String getCountSqury(int userid);
}

