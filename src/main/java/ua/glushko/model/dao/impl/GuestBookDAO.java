package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuestBookDAO extends AbstractDAO<GuestBook> {

    private final String NAME_TABLE = "guest_book";
    private final String NAME_FIELD_ID = "id";
    private final String NAME_FIELD_ORDER_ID = "order_id";
    private final String NAME_FIELD_USER_NAME = "user_name";
    private final String NAME_FIELD_DESCRIPTION = "description";
    private final String NAME_FIELD_ACTION_DATE = "action_date";
    private final String NAME_FIELD_MEMO = "memo";
    private static final GuestBookDAO dao = new GuestBookDAO();

    private GuestBookDAO() {
        super();
    }

    public static GuestBookDAO getInstance() {
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
                .append(NAME_FIELD_ORDER_ID).append(",")
                .append(NAME_FIELD_USER_NAME).append(",")
                .append(NAME_FIELD_DESCRIPTION).append(",")
                .append(NAME_FIELD_ACTION_DATE).append(",")
                .append(NAME_FIELD_MEMO).toString();
    }

    @Override
    protected void setGeneratedKey(GuestBook object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, GuestBook entity) throws SQLException {
        statement.setInt(1, entity.getOrderId());
        statement.setString(2, entity.getUserName());
        statement.setString(3, entity.getDecription());
        if(entity.getActionDate()!=null)
            statement.setTimestamp(4, new Timestamp(entity.getActionDate().getTime()));
        else
            statement.setDate(4,null);
        statement.setString(5, entity.getMemo());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, GuestBook object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<GuestBook> parseResultSet(ResultSet resultSet) throws SQLException {
        List<GuestBook> list = new ArrayList<>();
        while (resultSet.next()) {
            GuestBook item = new GuestBook();
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setOrderId(resultSet.getInt(NAME_FIELD_ORDER_ID));
            item.setUserName(resultSet.getString(NAME_FIELD_USER_NAME));
            item.setDecription(resultSet.getString(NAME_FIELD_DESCRIPTION));
            if(resultSet.getDate(NAME_FIELD_ACTION_DATE)!=null)
                item.setActionDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_ACTION_DATE).getTime()));
            item.setMemo(resultSet.getString(NAME_FIELD_MEMO));
            list.add(item);
        }
        return list;
    }

    protected String getCountSqury(int orderId) {
        return "select count(*) AS total from " + getTableName()+
                " where order_id="+orderId;
    }

    @Override
    protected String getSelectQuery() {
        return "select id, " + getFieldList() +
                " from " + getTableName();
    }

    public List<GuestBook> read() throws PersistException {
        List<GuestBook> list = Collections.emptyList();
        String sql = getSelectQuery()+" order by id desc";
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

    private String getSelectQuery(int from, int limit) {
        return "select id, " + getFieldList() +
                " from " + getTableName() +
                " order by id desc limit ?,? ";
    }

    public List<GuestBook> read(int start, int limit) throws PersistException {
        List<GuestBook> list = Collections.emptyList();
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

}