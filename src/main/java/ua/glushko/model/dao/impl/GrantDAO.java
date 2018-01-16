package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.exception.PersistException;
import ua.glushko.transaction.ConnectionWrapper;
import ua.glushko.transaction.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrantDAO extends AbstractDAO<Grant> {

    private static final GrantDAO dao = new GrantDAO();
    private final String NAME_TABLE = "grants";
    private final String NAME_FIELD_ID = "id";
    private final String NAME_FIELD_COMMAND = "command";
    private final String NAME_FIELD_MENU = "menu";
    private final String NAME_FIELD_ROLE = "role";
    private final String NAME_FIELD_ACTION = "action";
    private final String NAME_FIELD_SCOPE = "scope";

    private GrantDAO() {
        super();
    }

    public static GrantDAO getInstance() {
        return dao;
    }

    @Override
    protected void setGeneratedKey(Grant object, ResultSet generatedKeys) {
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Grant object) {
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Grant object) {
    }

    @Override
    protected String getTableName() {
        return NAME_TABLE;
    }

    @Override
    protected String getCountSqury(int userid) {
        return getCountSqury();
    }

    @Override
    protected String getFieldList() {
        StringBuilder builder = new StringBuilder();
        return builder
                .append(NAME_FIELD_COMMAND).append(",")
                .append(NAME_FIELD_MENU).append(",")
                .append(NAME_FIELD_ROLE).append(",")
                .append(NAME_FIELD_ACTION).append(",")
                .append(NAME_FIELD_SCOPE).toString();
    }

    @Override
    protected List<Grant> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Grant> list = new ArrayList<>();
        while (resultSet.next()) {
            Grant item = new Grant();
            item.setId(resultSet.getInt(NAME_FIELD_ID));
            item.setCommand(resultSet.getString(NAME_FIELD_COMMAND));
            item.setMenu(resultSet.getString(NAME_FIELD_MENU));
            item.setRole(resultSet.getString(NAME_FIELD_ROLE));
            item.setAction(resultSet.getString(NAME_FIELD_ACTION));
            item.setScope(resultSet.getString(NAME_FIELD_SCOPE));
            list.add(item);
        }
        return list;
    }

    @Override
    public void create(Grant object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Grant object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Grant delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Grant read(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Grant> read() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Grant> read(String role) throws PersistException {
        List<Grant> list = Collections.emptyList();
        String sql = getSelectQuery() +
                " where role=?";
        ResultSet resultSet = null;
        try (ConnectionWrapper con = TransactionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            prepareStatementForSelectByName(statement, role);
            resultSet = statement.executeQuery();
            list = parseResultSet(resultSet);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }


}