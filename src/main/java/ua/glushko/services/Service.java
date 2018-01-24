package ua.glushko.services;

import org.apache.log4j.Logger;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.List;

public class Service {
    protected final Logger LOGGER = Logger.getLogger(Service.class.getSimpleName());

    /** Get list of GenericEntity using */
    protected List<GenericEntity> getList(GenericDAO<GenericEntity> dao) throws TransactionException, DatabaseException {
        List<GenericEntity> read;
        try{
            TransactionManager.beginTransaction();
            read = dao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    /** Get list of GenericEntity using with limit */
    protected List<? extends GenericEntity> getList(AbstractDAO<? extends GenericEntity> dao, int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<? extends GenericEntity> read;
        try {
            TransactionManager.beginTransaction();
            read = dao.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    /** Get GenericEntity by id */
    protected <T extends GenericEntity> T getById(AbstractDAO<T> dao, int id) throws DaoException {
        return dao.read(id);
    }

    /** Update exist GenericEntity or create new */
    protected <T extends GenericEntity> void update(AbstractDAO<T> dao, T item) throws TransactionException, DatabaseException {
        try{
            TransactionManager.beginTransaction();
            if(item.getId()!=null && item.getId()!=0)
                dao.update(item);
            else
                dao.create(item);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    /** Delete exist GenericEntity */
    protected <T extends GenericEntity> void delete(AbstractDAO<T> dao, Integer id) throws TransactionException, DatabaseException {
        try{
            TransactionManager.beginTransaction();
            dao.delete(id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    /** Total of GenericEntity */
    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao) throws SQLException, TransactionException {
        Integer count;
        try{
            TransactionManager.beginTransaction();
            count = dao.count();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return count;
    }

    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao, int userId) throws SQLException, TransactionException {
        Integer count;
        try{
            TransactionManager.beginTransaction();
            count = dao.count(userId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return count;
    }
}
