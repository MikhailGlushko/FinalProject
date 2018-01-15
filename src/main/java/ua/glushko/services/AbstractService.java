package ua.glushko.services;

import org.apache.log4j.Logger;
import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.TransactionManager;

import java.util.List;

public class AbstractService {
    public final Logger LOGGER = Logger.getLogger(AbstractService.class.getSimpleName());

    protected List<? extends GenericEntity> getList(AbstractDAO<? extends GenericEntity> dao) throws PersistException, TransactionException {
        List<? extends GenericEntity> read;
        try{
            TransactionManager.beginTransaction();
            read = dao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    protected List<? extends GenericEntity> getList(AbstractDAO<? extends GenericEntity> dao, int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
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

    protected List<? extends GenericEntity> getList(AbstractDAO<? extends GenericEntity> dao, int page, int pagesCount, int rowsPerPage, int userId) throws PersistException, TransactionException {
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<? extends GenericEntity> read;
        try {
            TransactionManager.beginTransaction();
            read = ((OrderDAO)dao).read(start, limit, userId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    protected <T extends GenericEntity> T getById(AbstractDAO<T> dao, int id) throws PersistException, TransactionException {
        T read;
        try{
            TransactionManager.beginTransaction();
            read = dao.read(id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    protected <T extends GenericEntity> void update(AbstractDAO<T> dao, T item) throws PersistException, TransactionException {
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

    protected <T extends GenericEntity> void delete(AbstractDAO<T> dao, Integer id) throws PersistException, TransactionException {
        try{
            TransactionManager.beginTransaction();
            dao.delete(id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao) throws PersistException, TransactionException {
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

    protected <T extends GenericEntity> Integer count(AbstractDAO<T> dao, int userId) throws PersistException, TransactionException {
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
