package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.OrderHistory;
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

public class OrderHistoryDAO extends AbstractDAO<OrderHistory> {

    private final String NAME_TABLE = "orders_history";
    private final String NAME_FIELD_ID = "id";
    private final String NAME_FIELD_ORDER_ID = "order_id";
    private final String NAME_FIELD_USER_ID = "user_id";
    private final String NAME_FIELD_ACTION = "action";
    private final String NAME_FIELD_DESCRIPTION = "description";
    private final String NAME_FIELD_ACTION_DATE = "action_date";
    private final String NAME_FIELD_OLD_VALUE = "old_value";
    private final String NAME_FIELD_NEW_VALUE = "new_value";
    private final String NAME_FIELD_USER_NAME = "user_name";
    private static final OrderHistoryDAO dao = new OrderHistoryDAO();

    private OrderHistoryDAO() {
        super();
    }

    public static OrderHistoryDAO getInstance() {
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
                .append(NAME_FIELD_USER_ID).append(",")
                .append(NAME_FIELD_ACTION).append(",")
                .append(NAME_FIELD_DESCRIPTION).append(",")
                .append(NAME_FIELD_ACTION_DATE).append(",")
                .append(NAME_FIELD_OLD_VALUE).append(",")
                .append(NAME_FIELD_NEW_VALUE).toString();
    }

    @Override
    protected void setGeneratedKey(OrderHistory object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, OrderHistory entity) throws SQLException {
        statement.setInt(1, entity.getOrderId());
        statement.setInt(2, entity.getUserId());
        statement.setString(3, entity.getAction());
        statement.setString(4, entity.getDescription());
        if(entity.getActionDate()!=null)
            statement.setTimestamp(5, new Timestamp(entity.getActionDate().getTime()));
        else
            statement.setDate(5,null);
        statement.setString(6, entity.getOldValue());
        statement.setString(7, entity.getNewValue());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, OrderHistory object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<OrderHistory> parseResultSet(ResultSet resultSet) throws SQLException {
        List<OrderHistory> list = new ArrayList<>();
        while (resultSet.next()) {
            OrderHistory item = new OrderHistory();
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setOrderId(resultSet.getInt(NAME_FIELD_ORDER_ID));
            item.setUserId(resultSet.getInt(NAME_FIELD_USER_ID));
            item.setAction(resultSet.getString(NAME_FIELD_ACTION));
            item.setDescription(resultSet.getString(NAME_FIELD_DESCRIPTION));
            if(resultSet.getDate(NAME_FIELD_ACTION_DATE)!=null)
                item.setActionDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_ACTION_DATE).getTime()));
            item.setOldValue(resultSet.getString(NAME_FIELD_OLD_VALUE));
            item.setNewValue(resultSet.getString(NAME_FIELD_NEW_VALUE));
            item.setUserName(resultSet.getString(NAME_FIELD_USER_NAME));
            list.add(item);
        }
        return list;
    }

    public List<OrderHistory> read(int start, int limit, int orderId) throws PersistException {
        List<OrderHistory> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.orders_history a\n" +
                "left join users b on a.user_id=b.id \n" +
                "where a.order_id=?\n"+
                "order by action_date desc\n" +
                "limit ?,?;";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, start);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public List<OrderHistory> read() throws PersistException {
        List<OrderHistory> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.orders_history a\n" +
                "left join users b on a.user_id=b.id \n" +
                "order by action_date desc;";
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

    @Override
    public List<OrderHistory> read(int start, int limit) throws PersistException {
        List<OrderHistory> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.orders_history a\n" +
                "left join users b on a.user_id=b.id \n" +
                "order by action_date desc\n" +
                "limit ?,?;";
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

    @Override
    public OrderHistory read(int id) throws PersistException {
        List<OrderHistory> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.orders_history a\n" +
                "left join users b on a.user_id=b.id \n" +
                "where a.id=?\n"+
                "order by action_date desc";
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

    protected String getCountQuery(int orderId) {
        return "select count(*) AS total from " + getTableName()+
                " where order_Id = "+orderId;
    }
}