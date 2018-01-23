package ua.glushko.model.dao.impl;

import ua.glushko.exception.DaoException;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.entity.OrderQue;
import ua.glushko.model.entity.UserRole;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderQueDAO extends AbstractDAO<OrderQue> {

    private final String NAME_TABLE = "order_que";
    private final String NAME_FIELD_ID = "id";
    private final String NAME_FIELD_ORDER_ID = "order_id";
    private final String NAME_FIELD_ROLE = "role";
    private final String NAME_FIELD_EMPLOYEE_ID = "employee_id";
    private final String NAME_FIELD_CREATE = "create_date";
    private final String NAME_FIELD_CLOSE = "close_date";
    private final String NAME_FIELD_MESSAGE = "message";

    private static final OrderQueDAO dao = new OrderQueDAO();

    private OrderQueDAO() {
        super();
    }

    public static OrderQueDAO getInstance() {
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
                .append(NAME_FIELD_ROLE).append(",")
                .append(NAME_FIELD_EMPLOYEE_ID).append(",")
                .append(NAME_FIELD_CREATE).append(",")
                .append(NAME_FIELD_CLOSE).append(",")
                .append(NAME_FIELD_MESSAGE).toString();
    }

    @Override
    protected void setGeneratedKey(OrderQue object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, OrderQue entity) throws SQLException {
        statement.setInt(1, entity.getOrderId());
        if(entity.getRole()!=null)
            statement.setString(2, entity.getRole().toString());
        else
            statement.setString(2, UserRole.MANAGER.toString());
        statement.setInt(3, entity.getEmployeeId());
        if(entity.getCreate()!=null)
            statement.setTimestamp(4, new Timestamp(entity.getCreate().getTime()));
        else
            statement.setDate(4,null);
        if(entity.getClose()!=null)
            statement.setTimestamp(5, new Timestamp(entity.getClose().getTime()));
        else
            statement.setDate(5,null);
        statement.setString(6, entity.getMessage());

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, OrderQue object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<OrderQue> parseResultSet(ResultSet resultSet) throws SQLException {
        List<OrderQue> list = new ArrayList<>();
        while (resultSet.next()) {
            OrderQue item = new OrderQue();
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setOrderId(resultSet.getInt(NAME_FIELD_ORDER_ID));
            item.setRole(resultSet.getString(NAME_FIELD_ROLE));
            item.setEmployeeId(resultSet.getInt(NAME_FIELD_EMPLOYEE_ID));
            if(resultSet.getDate(NAME_FIELD_CREATE)!=null)
                item.setCreate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_CREATE).getTime()));
            if(resultSet.getDate(NAME_FIELD_CLOSE)!=null)
                item.setClose(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_CLOSE).getTime()));
            item.setMessage(resultSet.getString(NAME_FIELD_MESSAGE));
            list.add(item);
        }
        return list;
    }

    protected String getCountQuery(int orderId) {
        return "select count(*) AS total from " + getTableName()+
                " where order_id="+orderId;
    }

    @Override
    protected String getSelectQuery() {
        return "select id, " + getFieldList() +
                " from " + getTableName();
    }

    public List<OrderQue> read() throws DaoException {
        List<OrderQue> list = Collections.emptyList();
        String sql = getSelectQuery()+" order by id desc";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            setTitles(statement.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    private String getSelectQuery(int from, int limit) {
        return "select id, " + getFieldList() +
                " from " + getTableName() +
                " order by id desc limit ?,? ";
    }

    public List<OrderQue> read(int start, int limit) throws DaoException {
        List<OrderQue> list = Collections.emptyList();
        String sql = getSelectQuery(start, limit);
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, start);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }
}