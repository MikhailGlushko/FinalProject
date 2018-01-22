package ua.glushko.transaction;

import ua.glushko.model.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection Poll
 */
public class ConnectionPool {

    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool();
    private static DataSource dataSource;

    private ConnectionPool() {
    }

    public static ConnectionPool getConnectionPool() {
        return CONNECTION_POOL;
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("No Database");
        }
    }

    public void setDataSource(DataSource dataSource) {
        ConnectionPool.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
