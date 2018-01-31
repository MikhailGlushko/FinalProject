package ua.glushko.services;

import org.apache.log4j.Logger;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;

/**
 * CRUD Parent Service for all Services
 * @author Mikhail Glushko
 * @version 1.0
 */
abstract public class Service {
    protected final Logger LOGGER = Logger.getLogger(Service.class.getSimpleName());

    /** Get GenericEntity by id */
    protected <T extends GenericEntity> T getById(AbstractDAO<T> dao, int id) throws SQLException {
        return dao.read(id);
    }

    /** Update exist GenericEntity or create new */
    protected <T extends GenericEntity> void update(AbstractDAO<T> dao, T item) throws TransactionException, SQLException {
        try {
            TransactionManager.beginTransaction();
            if (item.getId() != null && item.getId() != 0)
                dao.update(item);
            else
                dao.create(item);
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
    }

    /** Delete exist GenericEntity */
    protected <T extends GenericEntity> void delete(AbstractDAO<T> dao, Integer id) throws TransactionException, SQLException {
        try{
            TransactionManager.beginTransaction();
            dao.delete(id);
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
    }

    /** Total of GenericEntity */
    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao) throws SQLException, TransactionException {
        Integer count=null;
        try{
            TransactionManager.beginTransaction();
            count = dao.count();
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
        return count;
    }

    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao, int userId) throws SQLException, TransactionException {
        Integer count =null;
        try{
            TransactionManager.beginTransaction();
            count = dao.count(userId);
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
        return count;
    }
}
