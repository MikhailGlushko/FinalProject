package ua.glushko.model.exception;

import org.apache.log4j.Logger;

public class ParameterException extends Exception {

    private static final Logger logger = Logger.getLogger(PersistException.class.getSimpleName());

    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
        logger.error(message.concat(" ( ").concat(getStackTrace()[1].toString()).concat(" )"));
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
        logger.error(cause.getStackTrace()[1].toString());
    }

    public ParameterException(Throwable cause) {
        super(cause);
        logger.error(cause.getStackTrace()[1].toString());
    }

    public ParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.error(message);
        logger.error(cause.getStackTrace()[1].toString());
    }
}
