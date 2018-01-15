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

/**
 * Абстрактная реализация DAO.
 * Использованы паттерны ДАО и Шаблонный метод. Наследники реализуют специфичную им логику и поведение,
 * в то время как вся общая логика реализована в предке.
 */
abstract public class AbstractDAO<T extends GenericEntity> implements GenericDAO<T> {
    protected static final Logger logger = Logger.getLogger(AbstractDAO.class.getSimpleName());

    private List<String> titles;
    protected AbstractDAO() {
        super();
    }

    /**
     * создает новую запись в базе данных
     * при успешном завершение устанавливает поле id в объекте значением из базы данных уникального ключа
     * при неуспешном завершении - выбрасывает исключение
     */
    @Override
    public void create(T object) throws PersistException {
        String sql = getCreateQuery();
        ResultSet generatedKeys = null;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForCreate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows == 0) {                        // должны быть записи но их нет
                throw new PersistException("Can not insert record to database. Operation aborted.");
            }
            generatedKeys = statement.getGeneratedKeys();
            setGeneratedKey(object, generatedKeys);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    /**
     * получить сгенерированый базой ключь и внедрить его в объект
     */
    protected abstract void setGeneratedKey(T object, ResultSet generatedKeys) throws SQLException;

    /**
     * настроить стайтмент для запроса создания записи
     */
    protected abstract void prepareStatementForCreate(PreparedStatement statement, T object) throws SQLException;

    /**
     * получить текст запроса на создание
     */
    private String getCreateQuery() {
        return "insert into " + getTableName() +
                " (" + getFieldList() +
                ") values " + getFieldListForInsert(getFieldList());
    }

    /**
     * обновляем запись в базе данных на основании объекта
     * при успешном завершении возвращаем измененный объект
     * при неуспешном завершении - выбрасываем исключение
     */
    @Override
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int effectedRows = statement.executeUpdate();
            if (effectedRows != 1) {
                throw new PersistException("Can not update record. Operation aborted.");
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    /**
     * готовим стейтмент для запроса по обновлению
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
     * удаляем запись из базы даных по ключу
     * при успешном удалении возвращаем запись которую удалили
     * при неуспешном удалении - выбрасываем исключение
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
     * готовим стейтмент для запроса на удаление
     */
    private void prepareStatementForDeleteByKey(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * удаляем все записи из таблицы.
     * при успешном удалении - вовращаем список записей, клторые предстояло удалить
     * при неуспешном - выбрасываем исключение
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
     * получаем тест запроса на удаление
     */
    private String getDeleteQuery() {
        return "delete from " + getTableName();
    }

    /**
     * читаем одну запись с базы данных по ключу
     * при успешном чтении - возвращаем полученную запись
     * при неуспешном чтении - выбрасываем исключение
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
     * заносим полученный результат в список сущностей
     */
    protected abstract List<T> parseResultSet(ResultSet resultSet) throws SQLException;

    /**
     * готовим стейтмент для запроса выборки по ключу
     */
    protected void prepareStatementForSelectById(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * получаем список записей по фильтру имени
     * при успешном выполнении - возвращаем списое, полученных записей
     * при неуспешном выполнении - выбрасываем исключение
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
     * готовим стейтмент для запроса выборки
     */
    protected void prepareStatementForSelectByName(PreparedStatement statement, String name) throws SQLException {
        statement.setString(1, name);
    }

    /**
     * получаем из базы данных список всех записей для указанной сущности
     * при успешном выполнении возвращаем список объектов
     * при не успешном выполнении - выбрасываем исключение
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
     * получаем текст запроса выборки даных с таблицы
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
     * на основании списка полей строит списсок знаков вопросов для INSERT
     * name, value -> ?,?
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
     * на основании списка полей строит список параметров для UPDATE
     * name, value -> name=?, value=?
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

