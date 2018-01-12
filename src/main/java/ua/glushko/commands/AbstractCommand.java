package ua.glushko.commands;

import org.apache.log4j.Logger;

public abstract class AbstractCommand implements Command {
    public static final Logger LOGGER = Logger.getLogger(Command.class.getSimpleName());

    public static final String PARAM_NAME_COMMAND = "command";
    public static final String PARAM_NAME_NULL_PAGE = "nullpage";
    public static final String PARAM_NAME_ERROR_MESSAGE = "errorMessage";
    public static final String PARAM_NAME_LOCALE  = "locale";

    public static final String PARAM_NAME_USER    = "user_detail";
    public static final String PARAM_NAME_SERVICE = "service_detail";

    public static final String PARAM_NAME_USER_ID = "user_id";
    public static final String PARAM_NAME_USER_LOGIN = "user_login";
    public static final String PARAM_NAME_USER_PASSWORD  = "user_password";
    public static final String PARAM_NAME_USER_PASSWORD2 = "user_password2";
    public static final String PARAM_NAME_USER_SECRET = "user_secret";
    public static final String PARAM_NAME_USER_NAME = "user_name";
    public static final String PARAM_NAME_USER_ROLE = "user_role";
    public static final String PARAM_NAME_USER_STATUS = "user_status";
    public static final String PARAM_NAME_USER_LAST_LOGIN = "user_last_login";
    public static final String PARAM_NAME_USER_EMAIL = "user_email";
    public static final String PARAM_NAME_USER_PHONE = "user_phone";
    public static final String PARAM_NAME_USER_GRANTS = "user_grants";

    public static final String PARAM_NAME_SERVICE_ID = "service_id";
    public static final String PARAM_NAME_SERVICE_NAME = "service_name";
    public static final String PARAM_NAME_SERVICE_NAME_RU = "service_name_ru";
    public static final String PARAM_NAME_SERVICE_PARENT = "service_parent";

    public static final String PARAM_NAME_PAGE  = "page";

    public static final String PARAM_NAME_USER_LIST = "users_list";
    public static final String PARAM_NAME_USER_LIST_TITLE = "users_list_head";

    public static final String PARAM_NAME_SERVICE_LIST = "services_list";
    public static final String PARAM_NAME_SERVICE_LIST_TITLE = "services_list_head";

    public static final String PARAM_NAME_PAGES_COUNT = "pagescount";
    public static final String PARAM_NAME_ROWS_COUNT  = "rowscount";

    public static final String VALUE_LOCALE_RU = "ru";
    public static final String VALUE_LOCALE_EN = "en";

    public static final String COOKIE_NAME_LOCALE = "locale";

    public static final String PATH_PAGE_INDEX = "path.page.index";
    public static final String PATH_PAGE_WELCOME = "path.page.welcome";
    public static final String PATH_PAGE_MAIN = "path.page.main";
    public static final String PATH_PAGE_ERROR = "path.page.error";
    public static final String PATH_PAGE_LOGIN = "path.page.login";
    public static final String PATH_PAGE_REGISTER = "path.page.register";
    public static final String PATH_PAGE_RESET_PASSWORD = "path.page.resetpassword";
    public static final String PATH_PAGE_USERS = "path.page.users";
    public static final String PATH_PAGE_USERS_DETAIL = "path.page.usersdetail";
    public static final String PATH_PAGE_USERS_SETUP = "path.page.userssetup";

    public static final String PATH_PAGE_SERVICES = "path.page.services";
    public static final String PATH_PAGE_SERVICES_DETAIL = "path.page.servicesdetail";

    public static final String MESSAGE_USER_STATUS = "user.status.";
    public static final String MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD = "user.incorrectLoginOrPassword";
    public static final String MESSAGE_USER_IS_REGISTERED = "user.isRegistered";
    public static final String MESSAGE_USER_PSSWORD_WAS_SEND = "user.passwordSent";
    public static final String MESSAGE_USER_ALREADY_EXIST = "user.alreadyexist";
    public static final String MESSAGE_USER_INCORRECT_DATA = "user.incorrectdata";
    public static final String MESSAGE_NULL_PAGE = "message.nullpage";

    public static final String PROPERTY_NAME_BROWSER_PAGES_COUNT = "browser.pages.count";
    public static final String PROPERTY_NAME_BROWSER_ROWS_COUNT = "browser.rows.count";
}
