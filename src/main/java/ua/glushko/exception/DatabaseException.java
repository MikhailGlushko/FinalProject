package ua.glushko.exception;

import java.sql.SQLException;

public class DatabaseException extends SQLException {

    public DatabaseException(String reason) {
        super(reason);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    DatabaseException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
