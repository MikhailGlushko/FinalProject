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
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {

    private final String NAME_TABLE = "orders";
    private final String NAME_FIELD_ID = "id";
    private final String NAME_FIELD_DESCRIPTION_SHORT = "description_short";
    private final String NAME_FIELD_DESCRIPTION_DETAIL = "description_detail";
    private final String NAME_FIELD_REPAIR_SERVICE = "repair_service";
    private final String NAME_FIELD_CITY = "city";
    private final String NAME_FIELD_STREET = "street";
    private final String NAME_FIELD_ORDER_DATE = "order_date";
    private final String NAME_FIELD_EXPECTED_DATE = "expected_date";
    private final String NAME_FIELD_APPLIANCE = "appliance";
    private final String NAME_FIELD_PRICE = "price";
    private final String NAME_FIELD_USER_ID = "user_id";
    private final String NAME_FIELD_MEMO = "memo";
    private final String NAME_FIELD_STATUS = "status";
    private final String NAME_FIELD_EMPLOYEE_ID = "employee_id";
    private final String NAME_FIELD_USER_NAME = "user_name";
    private final String NAME_FIELD_EMPLOYEE_NAME = "employee_name";

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
                .append(NAME_FIELD_DESCRIPTION_SHORT).append(",")
                .append(NAME_FIELD_DESCRIPTION_DETAIL).append(",")
                .append(NAME_FIELD_REPAIR_SERVICE).append(",")
                .append(NAME_FIELD_CITY).append(",")
                .append(NAME_FIELD_STREET).append(",")
                .append(NAME_FIELD_ORDER_DATE).append(",")
                .append(NAME_FIELD_EXPECTED_DATE).append(",")
                .append(NAME_FIELD_APPLIANCE).append(",")
                .append(NAME_FIELD_PRICE).append(",")
                .append(NAME_FIELD_USER_ID).append(",")
                .append(NAME_FIELD_MEMO).append(",")
                .append(NAME_FIELD_STATUS).append(",")
                .append(NAME_FIELD_EMPLOYEE_ID).toString();
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
        if(entity.getExpectedDate()!=null)
            statement.setTimestamp(7, new Timestamp(entity.getExpectedDate().getTime()));
        else
            statement.setDate(7,null);
        statement.setString(8, entity.getAppliance());
        statement.setDouble(9, entity.getPrice());
        statement.setInt(10, entity.getUserId());
        statement.setString(11, entity.getMemo());
        statement.setString(12, entity.getStatus().name());
        statement.setInt(13, entity.getEmployeeId());
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
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setDescriptionShort(resultSet.getString(NAME_FIELD_DESCRIPTION_SHORT));
            item.setDescriptionDetail(resultSet.getString(NAME_FIELD_DESCRIPTION_DETAIL));
            item.setRepairService(resultSet.getInt(NAME_FIELD_REPAIR_SERVICE));
            item.setCity(resultSet.getString(NAME_FIELD_CITY));
            item.setStreet(resultSet.getString(NAME_FIELD_STREET));
            if(resultSet.getDate(NAME_FIELD_ORDER_DATE)!=null)
                item.setOrderDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_ORDER_DATE).getTime()));
            if(resultSet.getDate(NAME_FIELD_EXPECTED_DATE)!=null)
                item.setExpectedDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_EXPECTED_DATE).getTime()));
            item.setAppliance(resultSet.getString(NAME_FIELD_APPLIANCE));
            item.setPrice(resultSet.getDouble(NAME_FIELD_PRICE));
            item.setUserId(resultSet.getInt(NAME_FIELD_USER_ID));
            item.setMemo(resultSet.getString(NAME_FIELD_MEMO));
            item.setStatus(resultSet.getString(NAME_FIELD_STATUS));
            item.setEmployeeId(resultSet.getInt(NAME_FIELD_EMPLOYEE_ID));
            item.setUserName(resultSet.getString(NAME_FIELD_USER_NAME));
            item.setEmployeeName(resultSet.getString(NAME_FIELD_EMPLOYEE_NAME));
            list.add(item);
        }
        return list;
    }

    public List<Order> read(int start, int limit, int userId) throws PersistException {
        List<Order> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where (a.user_id=? or a.employee_id=?)\n"+
                "order by order_date desc, status\n" +
                "limit ?,?;";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, start);
            statement.setInt(4, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public List<Order> read() throws PersistException {
        List<Order> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "order by order_date desc, status;";
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
    public List<Order> read(int start, int limit) throws PersistException {
        List<Order> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "order by order_date desc, status\n" +
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
    public Order read(int id) throws PersistException {
        List<Order> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where a.id=?\n"+
                "order by order_date desc, status";
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

    protected String getCountQuery(int userId) {
        return "select count(*) AS total from " + getTableName()+
                " where user_id="+userId+" or employee_id="+userId;
    }
}