package ua.glushko.exception;

import org.apache.log4j.Logger;
import ua.glushko.configaration.MessageManager;

import java.sql.SQLException;

/**
 * DAO Exception
 * @version 1.0
 * @author Mikhail Glushko
 */
public class DaoException extends DatabaseException {

    private static final Logger logger = Logger.getLogger(DaoException.class.getSimpleName());
    private final String PROPERTY_NAME_DAO_ERROR_CODE = "dao.errorCode";

    public DaoException(String message) {
        super(message);
        logger.error(message.concat(" ( ").concat(getStackTrace()[1].toString()).concat(" )"));
    }

    private DaoException(String message, Throwable cause) {
        super(message, cause);
        logger.error(String.format(MessageManager.getMessage(PROPERTY_NAME_DAO_ERROR_CODE),
                ((SQLException) cause).getErrorCode(),
                message,
                cause.getStackTrace()[1].toString()));
    }

    public DaoException(Throwable cause) {
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
