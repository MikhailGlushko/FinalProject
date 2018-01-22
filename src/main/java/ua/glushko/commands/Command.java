package ua.glushko.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    Logger LOGGER = Logger.getLogger(Command.class.getSimpleName());
    String PARAM_COMMAND = "command";
    String PARAM_NULL_PAGE = "nullpage";
    String PARAM_ERROR_MESSAGE = "errorMessage";
    String PARAM_LOCALE = "locale";
    String PARAM_PAGE = "page";
    String PARAM_LAST_PAGE = "lastpage";
    String PARAM_ACCESS = "access";
    String PARAM_PAGES_COUNT = "pagescount";
    String PARAM_ROWS_COUNT = "rowscount";
    String VALUE_LOCALE_RU = "ru";
    String VALUE_LOCALE_EN = "en";
    String COOKIE_LOCALE = "locale";
    String PATH_PAGE_INDEX = "path.page.index";
    String PATH_PAGE_WELCOME = "path.page.welcome";
    String PATH_PAGE_MAIN = "path.page.main";
    String PATH_PAGE_ERROR = "path.page.error";
    String PATH_PAGE_LOGIN = "path.page.login";
    String PATH_PAGE_REGISTER = "path.page.register";
    String PATH_PAGE_RECOVER = "path.page.recover";
    String PATH_PAGE_RESET_PASSWORD = "path.page.resetpassword";
    String MESSAGE_NULL_PAGE = "message.nullpage";
    String PROPERTY_NAME_BROWSER_PAGES_COUNT = "browser.pages.count";
    String PROPERTY_NAME_BROWSER_ROWS_COUNT = "browser.rows.count";

    CommandRouter execute(HttpServletRequest request, HttpServletResponse response);
}
