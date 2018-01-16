package ua.glushko.transaction;

import ua.glushko.model.exception.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Connection wrapper
 */
public class ConnectionWrapper implements AutoCloseable {
    private final Connection connection;
    private final boolean isTransaction;

    public ConnectionWrapper(Connection connection, boolean isTransaction) {
        this.connection = connection;
        this.isTransaction = isTransaction;
    }

    Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (!isTransaction) {
            connection.close();
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int returnGeneratedKeys) throws PersistException {
        try {
            return connection.prepareStatement(sql, returnGeneratedKeys);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }
}
