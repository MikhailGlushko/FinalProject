package ua.glushko.model.dao.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.RepairService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairServiceDAO extends AbstractDAO<RepairService> {

    private final String NAME_TABLE = "repair_services";

    private final String NAME_FIELD_00 = "id";
    private final String NAME_FIELD_01 = "name";
    private final String NAME_FIELD_02 = "name_ru";
    private final String NAME_FIELD_03 = "parent";

    @Override
    protected String getSelectQuery() {
        return super.getSelectQuery();
    }

    private static final RepairServiceDAO dao = new RepairServiceDAO();

    private RepairServiceDAO() {
        super();
    }

    public static RepairServiceDAO getInstance() {
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
                .append(NAME_FIELD_03).toString();
    }

    @Override
    protected void setGeneratedKey(RepairService object, ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next())
            object.setId(generatedKeys.getInt(1));
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, RepairService entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getNameRu());
        statement.setInt(3, entity.getParent());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, RepairService object) throws SQLException {
        prepareStatementForCreate(statement, object);
        statement.setInt(statement.getParameterMetaData().getParameterCount(), object.getId());
    }

    @Override
    protected List<RepairService> parseResultSet(ResultSet resultSet) throws SQLException {
        List<RepairService> list = new ArrayList<>();
        while (resultSet.next()) {
            RepairService item = new RepairService();
            item.setId(resultSet.getInt(NAME_FIELD_00));
            item.setName(resultSet.getString(NAME_FIELD_01));
            item.setNameRu(resultSet.getString(NAME_FIELD_02));
            item.setParent(resultSet.getInt(NAME_FIELD_03));
            list.add(item);
        }
        return list;
    }


}