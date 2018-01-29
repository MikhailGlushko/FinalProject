package ua.glushko.exception;

import org.apache.log4j.Logger;

public class ParameterException extends Exception {

    private static final Logger logger = Logger.getLogger(ParameterException.class.getSimpleName());

    public ParameterException(String message) {
        super(message);
        logger.error(message.concat(" ( ").concat(getStackTrace()[1].toString()).concat(" )"));
    }

}
