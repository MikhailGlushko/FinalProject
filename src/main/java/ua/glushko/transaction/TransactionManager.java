package ua.glushko.transaction;

import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Transaction Manager
 */
public class TransactionManager {
    private static final ThreadLocal<ConnectionWrapper> threadLocal = new ThreadLocal<>();

    private TransactionManager() {
    }

    public static void beginTransaction() throws TransactionException, DatabaseException {
        if (Objects.nonNull(threadLocal.get()))
            throw new TransactionException();
        try {
            Connection connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ConnectionWrapper wrapper = new ConnectionWrapper(connection, true);
            threadLocal.set(wrapper);
        } catch (DatabaseException e){
            throw new DatabaseException(e);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static void endTransaction() throws TransactionException, DatabaseException {
        if (Objects.isNull(threadLocal.get()))
            throw new TransactionException();
        try {
            ConnectionWrapper wrapper = threadLocal.get();
            Connection connection = wrapper.getConnection();
            connection.commit();
            connection.close();
            threadLocal.set(null);
        } catch (DatabaseException e){
            throw new DatabaseException(e);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static void rollBack() throws DatabaseException {
        if (Objects.isNull(threadLocal.get()))
            return; // already closed
        try {
            ConnectionWrapper wrapper = threadLocal.get();
            Connection connection = wrapper.getConnection();
            connection.rollback();
            connection.close();
            threadLocal.set(null);
        } catch (DatabaseException e){
            throw new DatabaseException(e);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static ConnectionWrapper getConnection() throws DatabaseException {
        if (Objects.isNull(threadLocal.get())) {
            Connection connection = ConnectionPool.getConnection();
            return new ConnectionWrapper(connection, false);
        } else {
            return threadLocal.get();
        }
    }
}
