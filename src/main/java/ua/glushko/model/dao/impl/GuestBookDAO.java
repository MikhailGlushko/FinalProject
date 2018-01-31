package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.GuestBook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO to work with GuestBooks
 * @author Mikhail Glushko
 * @version 1.0
 */
public class GuestBookDAO extends AbstractDAO<GuestBook> {

    private final String NAME_FIELD_ORDER_ID = "order_id";
    private final String NAME_FIELD_USER_NAME = "user_name";
    private final String NAME_FIELD_DESCRIPTION = "description";
    private final String NAME_FIELD_ACTION_DATE = "action_date";
    private final String NAME_FIELD_MEMO = "memo";
    private final String NAME_FIELD_USER_ID = "user_id";
    private static final GuestBookDAO dao = new GuestBookDAO();

    private GuestBookDAO() {
        super();
    }

    public static GuestBookDAO getInstance() {
        return dao;
    }

    @Override
    protected String getTableName() {
        return "guest_book";
    }

    @Override
    protected String getFieldList() {
        return NAME_FIELD_ORDER_ID + "," +
                NAME_FIELD_USER_NAME + "," +
                NAME_FIELD_DESCRIPTION + "," +
                NAME_FIELD_ACTION_DATE + "," +
                NAME_FIELD_MEMO + "," +
                NAME_FIELD_USER_ID;
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
        statement.setString(3, entity.getDescription());
        if(entity.getActionDate()!=null)
            statement.setTimestamp(4, new Timestamp(entity.getActionDate().getTime()));
        else
            statement.setDate(4,null);
        statement.setString(5, entity.getMemo());
        statement.setInt(6, entity.getUserId());
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
            String NAME_FIELD_ID = "id";
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setOrderId(resultSet.getInt(NAME_FIELD_ORDER_ID));
            item.setUserName(resultSet.getString(NAME_FIELD_USER_NAME));
            item.setDescription(resultSet.getString(NAME_FIELD_DESCRIPTION));
            if(resultSet.getDate(NAME_FIELD_ACTION_DATE)!=null)
                item.setActionDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_ACTION_DATE).getTime()));
            item.setMemo(resultSet.getString(NAME_FIELD_MEMO));
            item.setUserId(resultSet.getInt(NAME_FIELD_USER_ID));
            list.add(item);
        }
        return list;
    }

    protected String getCountQuery(int orderId) {
        return "select count(*) AS total from " + getTableName()+
                " where "+NAME_FIELD_ORDER_ID+"="+orderId;
    }

    @Override
    protected String getSelectQuery() {
        return "select "+NAME_FIELD_ID+", " + getFieldList() +
                " from " + getTableName() +
                " order by "+NAME_FIELD_ID+" desc";
    }

    protected String getSelectQueryWithLimit() {
        return "select "+NAME_FIELD_ID+", " + getFieldList() +
                " from " + getTableName() +
                " order by "+NAME_FIELD_ID+" desc limit ?,? ";
    }
}