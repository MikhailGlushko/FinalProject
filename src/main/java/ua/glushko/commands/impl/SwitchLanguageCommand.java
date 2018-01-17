package ua.glushko.commands.impl;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Switch language
 */
public class SwitchLanguageCommand extends Command {

    public static final String REFERRER = "referer";

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String lang = request.getParameter(PARAM_NAME_LOCALE);
        String locale = (lang != null && lang.equals(VALUE_LOCALE_EN)) ? VALUE_LOCALE_EN : VALUE_LOCALE_RU;
        session.setAttribute(PARAM_NAME_LOCALE, locale);
        response.addCookie(new Cookie(PARAM_NAME_LOCALE, locale));
        String page = request.getHeader(REFERRER);
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
    }
}
