package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.Order;
import ua.glushko.exception.DaoException;
import ua.glushko.model.entity.OrderStats;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * DAO to work with Orders
 * @author Mikhail Glushko
 * @version 1.0
 */
public class OrderDAO extends AbstractDAO<Order> {

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
    private final String NAME_FIELD_MANAGER_ID = "manager_id";
    private final String NAME_FIELD_CHANGE_DATE = "change_date";

    private static final OrderDAO dao = new OrderDAO();

    private OrderDAO() {
        super();
    }

    public static OrderDAO getInstance() {
        return dao;
    }

    @Override
    protected String getTableName() {
        return "orders";
    }

    @Override
    protected String getFieldList() {
        return NAME_FIELD_DESCRIPTION_SHORT + "," +
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
                NAME_FIELD_MANAGER_ID+ "," +
                NAME_FIELD_CHANGE_DATE;
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
        if(entity.getChangeDateDate()!=null)
            statement.setTimestamp(15, new Timestamp(entity.getChangeDateDate().getTime()));
        else
            statement.setDate(15,null);
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
            String NAME_FIELD_ID = "id";
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
            String NAME_FIELD_USER_NAME = "user_name";
            item.setUserName(resultSet.getString(NAME_FIELD_USER_NAME));
            String NAME_FIELD_EMPLOYEE_NAME = "employee_name";
            item.setEmployeeName(resultSet.getString(NAME_FIELD_EMPLOYEE_NAME));
            item.setManagerId(resultSet.getInt(NAME_FIELD_MANAGER_ID));
            if(resultSet.getDate(NAME_FIELD_CHANGE_DATE)!=null)
                item.setChangeDateDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_CHANGE_DATE).getTime()));
            list.add(item);
        }
        return list;
    }

    public Order take(OrderStatus status) throws DaoException{
        String sql = "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where a.status=? and (employee_id is null or employee_id=0)\n"+
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
                "order by status,id desc\n" +
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
                "order by status,id desc;";
    }

    @Override
    protected String getSelectQueryWithLimit() {
        return "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "order by status,id desc\n" +
                "limit ?,?;";
    }

    @Override
    protected String getSelectQueryById(){
        return "SELECT a.*, b.name as `user_name`, coalesce(c.name,'NOT ASSIGNED') as `employee_name`\n" +
                "FROM repair_agency.orders a\n" +
                "left join users b on a.user_id=b.id \n" +
                "left join users c on a.employee_id=c.id\n" +
                "where a.id=?\n"+
                "order by status,id desc";
    }

    protected String getCountQuery(int userId) {
        return "select count(*) AS total from " + getTableName()+
                " where user_id="+userId+" or employee_id="+userId;
    }

    public Map<OrderStatus, Map<OrderStats,Integer>> getTotal(Integer userId) throws DaoException {
        Map<OrderStatus,Map<OrderStats,Integer>> result = new LinkedHashMap<>();
        String sql="select a.status, coalesce(a.total,0) as `total`, coalesce(b.new,0) as `new`, coalesce(c.todays,0) as `today`, \n" +
                "coalesce(d.owner,0) as `owner`,coalesce(e.execution,0) as `execution`, coalesce(f.noemployee,0) as `noemployee`, \n" +
                "coalesce(g.current,0) as `current`, coalesce(h.countall,0) as `countall` \n"+
                "from (select count(*) as `total`, status from repair_agency.orders group by status) a\n" +
                "left join (select count(*) as `new`, status from repair_agency.orders where order_date>=current_date() group by status) b\n" +
                "on a.status=b.status\n" +
                "left join (select count(*) as `todays`, status from repair_agency.orders where change_date>=current_date() group by status) c\n" +
                "on a.status=c.status\n" +
                "left join (select count(*) as `owner`, status from repair_agency.orders where user_id=?  group by status) d\n" +
                "on a.status = d.status\n" +
                "left join (select count(*) as `execution`, status from repair_agency.orders where user_id=? and (status='NEW' or status='CONFIRMATION' or status='PAYMENT' or status='CLOSE' or status='REJECT') or employee_id=? and (status='VERIFICATION' or status='ESTIMATE' or status='PROGRESS' or status='COMPLETE')  group by status) e\n" +
                "on a.status = e.status\n" +
                "left join (select count(*) as `noemployee`, status from repair_agency.orders where (employee_id is null or employee_id=0) group by status) f\n" +
                "on a.status=f.status\n" +
                "left join (select count(*) as `current`, status from  repair_agency.orders where (employee_id=? or user_id=?) group by status) g\n"+
                "on a.status=g.status\n" +
                "left join (select count(*) as `countall` from repair_agency.orders) h\n"+
                "on true\n"+
                "group by a.status;";

        try(ConnectionWrapper con = TransactionManager.getConnection();
            PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,userId);
            statement.setInt(2,userId);
            statement.setInt(3,userId);
            statement.setInt(4,userId);
            statement.setInt(5,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String status = resultSet.getString(1);
                if(Objects.nonNull(status)) {
                    OrderStatus key = OrderStatus.valueOf(status);
                    Map<OrderStats,Integer> value = new HashMap<>();
                    value.put(OrderStats.STATUS,resultSet.getInt(2));
                    value.put(OrderStats.NEW,resultSet.getInt(3));
                    value.put(OrderStats.TODAY,resultSet.getInt(4));
                    value.put(OrderStats.OWNER,resultSet.getInt(5));
                    value.put(OrderStats.EXECUTION,resultSet.getInt(6));
                    value.put(OrderStats.NO_EMPLOYEE,resultSet.getInt(7));
                    value.put(OrderStats.CURRENT_USER,resultSet.getInt(8));
                    value.put(OrderStats.ALL,resultSet.getInt(9));
                    result.put(key,value);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }
}
