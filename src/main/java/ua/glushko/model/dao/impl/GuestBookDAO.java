package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.GuestBook;
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

public class GuestBookDAO extends AbstractDAO<GuestBook> {

    private final String NAME_TABLE = "guest_book";

    private final String NAME_FIELD_00 = "id";
    private final String NAME_FIELD_01 = "order_id";
    private final String NAME_FIELD_02 = "user_id";
    private final String NAME_FIELD_03 = "description";
    private final String NAME_FIELD_04 = "date";
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
                .append(NAME_FIELD_01).append(",")
                .append(NAME_FIELD_02).append(",")
                .append(NAME_FIELD_03).append(",")
                .append(NAME_FIELD_04).toString();
    }

    @Override
    protected void setGeneratedKey(GuestBook object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, GuestBook entity) throws SQLException {
        statement.setInt(1, entity.getUserId());
        statement.setInt(2, entity.getOrderId());
        if(entity.getDate()!=null)
            statement.setTimestamp(3, new Timestamp(entity.getDate().getTime()));
        else
            statement.setDate(3,null);
        statement.setString(6, entity.getDecription());
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
            item.setId(resultSet.getInt(NAME_FIELD_00));
            item.setUserId(resultSet.getInt(NAME_FIELD_01));
            item.setOrderId(resultSet.getInt(NAME_FIELD_02));
            if(resultSet.getDate(NAME_FIELD_03)!=null)
                item.setDate(new java.sql.Date(resultSet.getTimestamp(NAME_FIELD_03).getTime()));
            item.setDecription(resultSet.getString(NAME_FIELD_04));
            list.add(item);
        }
        return list;
    }

    public List<GuestBook> read(int start, int limit, int orderId) throws PersistException {
        List<GuestBook> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.guest_book a\n" +
                "left join users b on a.user_id=b.id \n" +
                "where a.order_id=?\n"+
                "order by order_date desc, status\n" +
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
    public List<GuestBook> read() throws PersistException {
        List<GuestBook> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.guest_book a\n" +
                "left join users b on a.user_id=b.id \n" +
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
    public List<GuestBook> read(int start, int limit) throws PersistException {
        List<GuestBook> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.guest_book a\n" +
                "left join users b on a.user_id=b.id \n" +
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
    public GuestBook read(int id) throws PersistException {
        List<GuestBook> list = Collections.emptyList();
        String sql = "SELECT a.*, b.name as `user_name`\n" +
                "FROM repair_agency.guest_book a\n" +
                "left join users b on a.user_id=b.id \n" +
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
}