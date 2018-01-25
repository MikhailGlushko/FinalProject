package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DaoException;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

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
    private final String NAME_FIELD_MANAGER_ID = "manager_id";

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
        String builder = NAME_FIELD_DESCRIPTION_SHORT + "," +
                NAME_FIELD_DESCRIPTION_DETAIL + "," +
                NAME_FIELD_REPAIR_SERVICE + "," +
                NAME_FIELD_CITY + "," +
                NAME_FIELD_STREET + "," +
                NAME_FIELD_ORDER_DATE + "," +
                NAME_FIELD_EXPECTED_DATE + "," +
                NAME_FIELD_APPLIANCE + "," +
                NAME_FIELD_PRICE + "," +
                NAME_FIELD_USER_ID + "," +
                NAME_FIELD_MEMO + "," +
                NAME_FIELD_STATUS + "," +
                NAME_FIELD_EMPLOYEE_ID+ "," +
                NAME_FIELD_MANAGER_ID;
        return builder;
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
        statement.setInt(14, entity.getManagerId());
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
            item.setManagerId(resultSet.getInt(NAME_FIELD_MANAGER_ID));
            list.add(item);
        }
        return list;
    }

    public Order take(OrderStatus status) throws DaoException{
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where a.status=? and employee_id is null\n"+
                "order by order_date asc, status\n" +
                "limit 1;";
        List<Order> list;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1,status.name());
            ResultSet resultSet = statement.executeQuery();
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        if (list.size() > 1) {
            throw new DaoException("Received more than one record.");
        }
        return list.iterator().next();
    }

    public List<Order> read(int start, int limit, int userId) throws DaoException {
        List<Order> list;
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
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    protected String getSelectQuery(){
        return "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "order by order_date desc, status;";
    }

    @Override
    protected String getSelectQuery(int start, int end) {
        return "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "order by order_date desc, status\n" +
                "limit ?,?;";
    }

    @Override
    protected String getSelectQueryById(){
        return "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where a.id=?\n"+
                "order by order_date desc, status";
    }

    protected String getCountQuery(int userId) {
        return "select count(*) AS total from " + getTableName()+
                " where user_id="+userId+" or employee_id="+userId;
    }

    public Integer countWithoutEmployeeByStatus(OrderStatus status) throws DaoException{
        String sql = "SELECT count(*) as total \n" +
                "FROM repair_agency.orders a\n" +
                "where (employee_id is null or employee_id=0) and status=?\n";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1,status.name());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("total");
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return 0;
    }

    public List<Order> readByStatus(int start, int limit) throws DaoException {
        List<Order> list;
        String sql =    "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='NEW' limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='CLOSE' limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='COMPLETE' limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='SUSPEND' limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='INWORK' limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='REJECT' limit ?,?)\n" +
                "order by status, id desc;";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, start);statement.setInt(2, limit);
            statement.setInt(3, start);statement.setInt(4, limit);
            statement.setInt(5, start);statement.setInt(6, limit);
            statement.setInt(7, start);statement.setInt(8, limit);
            statement.setInt(9, start);statement.setInt(10, limit);
            statement.setInt(11, start);statement.setInt(12, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    public List<Order> readByStatus(int start, int limit, int userId) throws DaoException {
        List<Order> list;
        String sql =    "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='NEW' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='CLOSE' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='COMPLETE' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='SUSPEND' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='INWORK' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "union\n" +
                "select * from (SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name` FROM repair_agency.orders a left join users b on a.user_id=b.id left join users c on a.employee_id=c.id where a.status='REJECT' and (a.user_id=? or a.employee_id=?) limit ?,?)\n" +
                "order by status, id desc;";
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, userId);statement.setInt(2, userId);
            statement.setInt(3, start);statement.setInt(4, limit);
            statement.setInt(5, userId);statement.setInt(6, userId);
            statement.setInt(7, start);statement.setInt(8, limit);
            statement.setInt(9, userId);statement.setInt(10, userId);
            statement.setInt(11, start);statement.setInt(12, limit);
            statement.setInt(13, userId);statement.setInt(14, userId);
            statement.setInt(15, start);statement.setInt(16, limit);
            statement.setInt(17, userId);statement.setInt(18, userId);
            statement.setInt(19, start);statement.setInt(20, limit);
            statement.setInt(21, userId);statement.setInt(22, userId);
            statement.setInt(23, start);statement.setInt(24, limit);
            ResultSet resultSet = statement.executeQuery();
            setTitles(resultSet.getMetaData());
            list = parseResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    private Map<OrderStatus,Integer> getTotals(String sql) throws SQLException {
        Map<OrderStatus,Integer> result = new LinkedHashMap<>();
        try(ConnectionWrapper con = TransactionManager.getConnection();
            PreparedStatement statement = con.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int count = resultSet.getInt(1);
                String value = resultSet.getString(2);
                if(Objects.nonNull(value)) {
                    OrderStatus status = OrderStatus.valueOf(value);
                    result.put(status,count);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    public Map<OrderStatus,Integer> getTotalsByStatus() throws SQLException {
        String sql = "select count(*) as total, status from repair_agency.orders group by status";
        return getTotals(sql);
    }

    public Map<OrderStatus,Integer> getNewByStatus() throws SQLException {
        String sql = "select count(*) as new, status from repair_agency.orders where order_date=now() group by status";
        return getTotals(sql);    }
}
