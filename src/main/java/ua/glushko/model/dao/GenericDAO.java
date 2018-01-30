package ua.glushko.model.dao;

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
    void create(T object) throws DaoException;

    /**
     * Update record
     */
    void update(T object) throws DaoException;

    /**
     * Delete record
     */
    T delete(int id) throws DaoException;

    /**
     * Delete all records
     */
    void deleteAll() throws DaoException;

    /**
     * Get record by id
     */
    T read(int id) throws DaoException;

    /**
     * Get all records
     */
    List<T> read() throws DaoException;

    List<T> read(int start, int limit) throws DaoException;

    Integer count() throws SQLException;
}
