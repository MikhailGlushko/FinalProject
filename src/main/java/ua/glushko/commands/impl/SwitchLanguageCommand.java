package ua.glushko.commands.impl;

import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Переключение языка
 */
public class SwitchLanguageCommand extends AbstractCommand {

    private static final String REFERRER = "referer";

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String lang = request.getParameter(PARAM_NAME_LOCALE);
        String locale = (lang != null && lang.equals(VALUE_LOCALE_EN)) ? VALUE_LOCALE_EN : VALUE_LOCALE_RU;
        session.setAttribute(PARAM_NAME_LOCALE, locale);
        response.addCookie(new Cookie(PARAM_NAME_LOCALE, locale));

        LOGGER.debug(MessageManager.getMessage(MESSAGE_NULL_PAGE));
        String page = request.getHeader(REFERRER);

        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
    }
}
