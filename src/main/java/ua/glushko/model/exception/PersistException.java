package ua.glushko.model.exception;

import org.apache.log4j.Logger;
import ua.glushko.configaration.MessageManager;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class PersistException extends SQLException {

    private static final Logger logger = Logger.getLogger(PersistException.class.getSimpleName());
    private final String PROPERTY_NAME_DAO_ERROR_CODE = "dao.errorCode";

    public PersistException(String message) {
        super(message);
        logger.error(message.concat(" ( ").concat(getStackTrace()[1].toString()).concat(" )"));
    }

    private PersistException(String message, Throwable cause) {
        super(message, cause);
        logger.error(String.format(MessageManager.getMessage(PROPERTY_NAME_DAO_ERROR_CODE),
                ((SQLException) cause).getErrorCode(),
                message,
                cause.getStackTrace()[1].toString()));
    }

    public PersistException(Throwable cause) {
        super(cause);
        if (cause instanceof SQLException)
            logger.error(String.format(MessageManager.getMessage(PROPERTY_NAME_DAO_ERROR_CODE),
                    ((SQLException) cause).getErrorCode(),
                    cause.getMessage(),
                    cause.getStackTrace()[1].toString()));
        else
            logger.error(String.format(MessageManager.getMessage(PROPERTY_NAME_DAO_ERROR_CODE),
                    "0",
                    "",
                    cause.getStackTrace()[1].toString()));
    }
}
