package ua.glushko.utils.tags;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Objects;

import static ua.glushko.commands.Command.*;

@SuppressWarnings("serial")
public class LanguageSwitcherTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            Cookie[] cookies = request.getCookies();
            String cookieName = null;
            String cookieValue = null;
            if(Objects.nonNull(cookies))
            for (Cookie cookie:cookies) {
                cookieName = cookie.getName();
                if(Objects.nonNull(cookieName) && COOKIE_LOCALE.equals(cookieName) ) {
                    cookieValue = cookie.getValue();
                }
            }
            StringBuilder builder = new StringBuilder();

            if(Objects.isNull(cookieValue) || VALUE_LOCALE_EN.equals(cookieValue)){
                builder.append("<img src=\"../images/en.png\" alt=\"Switch to English\" width=\"20\"/>")
                        .append("<a href=\"/do?command=lang&amp;locale=ru\">")
                        .append("<img src=\"../images/ru.png\" alt=\"Переключится на Русский\" width=\"20\" class=\"langbutton\" />")
                        .append("</a>");
            } if(Objects.nonNull(cookieValue) && VALUE_LOCALE_RU.equals(cookieValue) ){
                builder.append("<a href=\"/do?command=lang&amp;locale=en\">")
                        .append("<img src=\"../images/en.png\" alt=\"Switch to English\" width=\"20\" class=\"langbutton\" />")
                        .append("</a>")
                        .append("<img src=\"../images/ru.png\" alt=\"Переключится на Русский\" width=\"20\"/>");
            }

            pageContext.getOut().write(builder.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}