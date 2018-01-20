package ua.glushko.tags;

import ua.glushko.authentification.Authentification;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.Grant;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;
import static ua.glushko.commands.CommandFactory.COMMAND_LOGOUT;

@SuppressWarnings("serial")
public class MainMenuTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {

            HttpSession session = pageContext.getSession();
            String locale = (String) session.getAttribute(PARAM_LOCALE);
            StringBuilder builder = new StringBuilder();

            builder.append("<div style=\"float: left; width: 75%\">")
                    .append("<a class=\"btn btn-default col-lg-2\" href=\"")
                    .append(ConfigurationManager.getProperty(PATH_PAGE_INDEX))
                    .append("\">")
                    .append(MessageManager.getMessage("main.home",locale))
                    .append("</a>");

            Object attributeUserGrants = session.getAttribute(Authentification.PARAM_GRANTS);
            List<Grant> grants = (List<Grant>) attributeUserGrants;

//            if(Objects.nonNull(grants)) {
//                builder.append("<div class=\"dropdown\" \"col-lg-9\">")
//                        .append("<button class=\"btn btn-default dropdown-toggle\" type=\"button\" data-toggle=\"dropdown\">")
//                        .append("Выпадающий список<span class=\"caret\"></span></button>")
//                        .append("<ul class=\"dropdown-menu\">");
//                for (Grant grant : grants) {
//                    if (Objects.nonNull(grant) && grant.getAction().contains("M")) {
//                        builder.append("<li><a href=\"/do?command=")
//                                .append(grant.getCommand())
//                                .append("\">")
//                                .append(MessageManager.getMessage("menu." + grant.getMenu(), locale))
//                                .append("</a></li>");
//                    }
//                }
//                builder.append("</ul></div>");
//            }

            if(Objects.nonNull(grants))
            for (Grant grant:grants) {
                if (Objects.nonNull(grant) && grant.getAction().contains("M")){
                    builder.append("<a class=\"btn btn-default col-lg-2 \" href=\"/do?command=")
                            .append(grant.getCommand())
                            .append("\">")
                            .append(MessageManager.getMessage("menu."+grant.getMenu(),locale))
                            .append("</a>");
                }
            }
            builder.append("</div>");
            builder.append("<div style=\"float: left; width: 25%;\" align=\"right\">");
            Object userName = pageContext.getSession().getAttribute(Authentification.PARAM_NAME_NAME);
            if(Objects.nonNull(userName)){
                builder.append("<a class=\"btn btn-default col-lg-6 col-lg-offset-6\" href=\"/do?command=")
                        .append(COMMAND_LOGOUT)
                        .append("\" style=\"margin-right: 0;\">")
                        .append(MessageManager.getMessage("main.logout",locale))
                        .append("</a>");
            } else {
                builder.append("<a class=\"btn btn-default col-lg-6\" href=\"")
                        .append(ConfigurationManager.getProperty(PATH_PAGE_LOGIN))
                        .append("\" style=\"margin-right: 0;\">")
                        .append(MessageManager.getMessage("main.login",locale))
                        .append("</a>")
                        .append("<a class=\"btn btn-default col-lg-6\" href=\"")
                        .append(ConfigurationManager.getProperty(PATH_PAGE_REGISTER))
                        .append("\" style=\"margin-right: 0;\">")
                        .append(MessageManager.getMessage("main.register",locale))
                        .append("</a>");
            }
            builder.append("</div>");

            pageContext.getOut().write(builder.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}