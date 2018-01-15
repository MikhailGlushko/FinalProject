package ua.glushko.commands;

import org.apache.log4j.Logger;

public abstract class Command implements GenericCommand {
    public static final Logger LOGGER = Logger.getLogger(GenericCommand.class.getSimpleName());

    public static final String PARAM_NAME_COMMAND = "command";
    protected static final String PARAM_NAME_NULL_PAGE = "nullpage";
    protected static final String PARAM_NAME_ERROR_MESSAGE = "errorMessage";
    public static final String PARAM_NAME_LOCALE  = "locale";

    public static final String PARAM_NAME_PAGE  = "page";
    protected static final String PARAM_NAME_LAST_PAGE = "lastpage";
    public static final String PARAM_NAME_ACCESS = "access";

    protected static final String PARAM_NAME_PAGES_COUNT = "pagescount";
    protected static final String PARAM_NAME_ROWS_COUNT  = "rowscount";

    public static final String VALUE_LOCALE_RU = "ru";
    public static final String VALUE_LOCALE_EN = "en";

    public static final String COOKIE_NAME_LOCALE = "locale";

    public static final String PATH_PAGE_INDEX = "path.page.index";
    protected static final String PATH_PAGE_WELCOME = "path.page.welcome";
    protected static final String PATH_PAGE_MAIN = "path.page.main";
    protected static final String PATH_PAGE_ERROR = "path.page.error";
    public static final String PATH_PAGE_LOGIN = "path.page.login";
    public static final String PATH_PAGE_REGISTER = "path.page.register";
    protected static final String PATH_PAGE_RECOVER = "path.page.recover";
    protected static final String PATH_PAGE_RESET_PASSWORD = "path.page.resetpassword";

    protected static final String MESSAGE_NULL_PAGE = "message.nullpage";

    public static final String PROPERTY_NAME_BROWSER_PAGES_COUNT = "browser.pages.count";
    public static final String PROPERTY_NAME_BROWSER_ROWS_COUNT = "browser.rows.count";
}
