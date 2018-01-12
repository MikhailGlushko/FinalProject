package ua.glushko.model.dao;

import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.exception.PersistException;

import java.util.List;

/**
 * Универсальный интерфейс управления объектами
 *
 * @param <T>  - объект
 * @author Mikhail Glushko
 * @version 1.0
 */
public interface GenericDAO<T extends GenericEntity> {

    /**
     * Создает новую запись
     */
    void create(T object) throws PersistException;

    /**
     * Обновляет существующую запись
     */
    void update(T object) throws PersistException;

    /**
     * Удаляет запись из базы данных
     */
    T delete(int id) throws PersistException;

    /**
     * Удаляет все записис из базы данных
     */
    void deleteAll() throws PersistException;

    /**
     * Получает запись из базы данных по ключу
     */
    T read(int id) throws PersistException;

    /**
     * Получает список всех записей из базы данных
     */
    List<T> read() throws PersistException;

    List<T> read(int start, int limit) throws PersistException;

    List<String> getTableHead();
}
