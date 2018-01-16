package ua.glushko.model.dao;

import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.exception.PersistException;

import java.util.List;


public interface GenericDAO<T extends GenericEntity> {

    /**
     * Create new record
     */
    void create(T object) throws PersistException;

    /**
     * Update record
     */
    void update(T object) throws PersistException;

    /**
     * Delete record
     */
    T delete(int id) throws PersistException;

    /**
     * Delete all records
     */
    void deleteAll() throws PersistException;

    /**
     * Get record by id
     */
    T read(int id) throws PersistException;

    /**
     * Get all records
     */
    List<T> read() throws PersistException;

    List<T> read(int start, int limit) throws PersistException;

    List<String> getTableHead();

    Integer count();
}
