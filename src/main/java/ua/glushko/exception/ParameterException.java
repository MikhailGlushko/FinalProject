package ua.glushko.exception;

import org.apache.log4j.Logger;

/**
 * Form parameters validation Exception
 * @author Mikhail Glushko
 * @version 1.0
 */
public class ParameterException extends Exception {

    private static final Logger logger = Logger.getLogger(ParameterException.class.getSimpleName());

    public ParameterException(String message) {
        super(message);
        logger.error(message.concat(" ( ").concat(getStackTrace()[1].toString()).concat(" )"));
    }

}
