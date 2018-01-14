package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.Order;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {

    private final String NAME_TABLE = "orders";

    private final String NAME_FIELD_00 = "id";
    private final String NAME_FIELD_01 = "description_short";
    private final String NAME_FIELD_02 = "description_detail";
    private final String NAME_FIELD_03 = "repair_service";
    private final String NAME_FIELD_04 = "city";
    private final String NAME_FIELD_05 = "street";
    private final String NAME_FIELD_06 = "order_date";
    private final String NAME_FIELD_07 = "time";
    private final String NAME_FIELD_08 = "appliance";
    private final String NAME_FIELD_09 = "paid_method";
    private final String NAME_FIELD_10 = "user_id";
    private final String NAME_FIELD_11 = "memo";

    @Override
    protected String getSelectQuery() {
        return super.getSelectQuery();
    }

    private static final OrderDAO dao = new OrderDAO();

    private OrderDAO() {
        super();
    }

    public static OrderDAO getInstance() {
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
                .append(NAME_FIELD_08).append(",")
                .append(NAME_FIELD_09).append(",")
                .append(NAME_FIELD_10).append(",")
                .append(NAME_FIELD_11).toString();
    }

    @Override
    protected void setGeneratedKey(Order object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Order entity) throws SQLException {
        statement.setString(1, entity.getDescriptionShort());
        statement.setString(2, entity.getDescriptionDetail());
        statement.setInt(3, entity.getRepairService());
        statement.setString(4, entity.getCity());
        statement.setString(5, entity.getStreet());
        if(entity.getOrderDate()!=null)
            statement.setTimestamp(6, new Timestamp(entity.getOrderDate().getTime()));
        else
            statement.setDate(6,null);
        statement.setString(7, entity.getTime());
        statement.setString(8, entity.getAppliance());
        statement.setString(9, entity.getPaidMethod());
        statement.setInt(10, entity.getUserId());
        statement.setString(11, entity.getMemo());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Order object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<Order> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Order> list = new ArrayList<>();
        while (resultSet.next()) {
            Order item = new Order();
            item.setId(resultSet.getInt(NAME_FIELD_00));
            item.setDescriptionShort(resultSet.getString(NAME_FIELD_01));
            item.setDescriptionDetail(resultSet.getString(NAME_FIELD_02));
            item.setRepairService(resultSet.getInt(NAME_FIELD_03));
            item.setCity(resultSet.getString(NAME_FIELD_04));
            item.setStreet(resultSet.getString(NAME_FIELD_05));
            if(resultSet.getDate(NAME_FIELD_06)!=null)
                item.setOrderDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_06).getTime()));
            item.setTime(resultSet.getString(NAME_FIELD_07));
            item.setAppliance(resultSet.getString(NAME_FIELD_08));
            item.setPaidMethod(resultSet.getString(NAME_FIELD_09));
            item.setUserId(resultSet.getInt(NAME_FIELD_10));
            item.setMemo(resultSet.getString(NAME_FIELD_11));
            list.add(item);
        }
        return list;
    }

    public List<Order> read(int start, int limit, int userId) throws PersistException {
        List<Order> list = Collections.emptyList();
        String sql = "select id, " + getFieldList() +
                " from " + getTableName() +
                " where user_id=? limit ?,? ";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, userId);
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




}