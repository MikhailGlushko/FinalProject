package ua.glushko.model.dao;

import ua.glushko.exception.DatabaseException;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.exception.DaoException;

import java.sql.SQLException;
import java.util.List;

/**
 * CRUD Interface for DAO
 * @author Mikhail Glushko
 * @version 1.0
 * @param <T>
 */
public interface GenericDAO<T extends GenericEntity> {

    /**
     * Create new record
     */
    void create(T object) throws SQLException;

    /**
     * Update record
     */
    void update(T object) throws SQLException;

    /**
     * Delete record
     */
    T delete(int id) throws SQLException;

    /**
     * Delete all records
     */
    void deleteAll() throws SQLException;

    /**
     * Get record by id
     */
    T read(int id) throws SQLException;

    /**
     * Get all records
     */
    List<T> read() throws DatabaseException, SQLException;

    List<T> read(int start, int limit) throws SQLException;

    Integer count() throws SQLException;
}
