package ua.glushko.transaction;

import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class TransactionManager {
    private static final ThreadLocal<ConnectionWrapper> threadLocal = new ThreadLocal<>();

    private TransactionManager() {
    }

    public static void beginTransaction() throws TransactionException, PersistException {
        if (Objects.nonNull(threadLocal.get()))
            throw new TransactionException();
        try {
            Connection connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ConnectionWrapper wrapper = new ConnectionWrapper(connection, true);
            threadLocal.set(wrapper);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    public static void endTransaction() throws TransactionException, PersistException {
        if (Objects.isNull(threadLocal.get()))
            throw new TransactionException();
        try {
            ConnectionWrapper wrapper = threadLocal.get();
            Connection connection = wrapper.getConnection();
            connection.commit();
            connection.close();
            threadLocal.set(null);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    public static void rollBack() throws TransactionException, PersistException {
        if (Objects.isNull(threadLocal.get()))
            return; // already closed
            //throw new TransactionException();
        try {
            ConnectionWrapper wrapper = threadLocal.get();
            Connection connection = wrapper.getConnection();
            connection.rollback();
            connection.close();
            threadLocal.set(null);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    public static ConnectionWrapper getConnection() throws SQLException {
        if (Objects.isNull(threadLocal.get())) {
            Connection connection = ConnectionPool.getConnection();
            return new ConnectionWrapper(connection, false);
        } else {
            return threadLocal.get();
        }
    }
}
