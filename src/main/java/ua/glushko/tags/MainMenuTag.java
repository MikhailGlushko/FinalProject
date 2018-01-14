package ua.glushko.tags;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
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
import static ua.glushko.commands.CommandFactory.COMMAND_NAME_LOGOUT;

@SuppressWarnings("serial")
public class MainMenuTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {

            HttpSession session = pageContext.getSession();
            String locale = (String) session.getAttribute(PARAM_NAME_LOCALE);
            StringBuilder builder = new StringBuilder();

            builder.append("<div style=\"float: left; width: 70%\">")
                    .append("<a class=\"mainmenubutton\" href=\"")
                    .append(ConfigurationManager.getProperty(PATH_PAGE_INDEX))
                    .append("\">")
                    .append(MessageManager.getMessage("main.home",locale))
                    .append("</a>");

            Object attributeUserGrants = session.getAttribute(Authentification.PARAM_NAME_GRANTS);
            //noinspection unchecked
            @SuppressWarnings("unchecked")
            List<Grant> grants = (List<Grant>) attributeUserGrants;
            if(Objects.nonNull(grants))
            for (Grant grant:grants) {
                if (Objects.nonNull(grant) && grant.getAction().contains("M")){
                    builder.append("<a class=\"mainmenubutton\" href=\"/do?command=")
                            .append(grant.getCommand())
                            .append("\">")
                            .append(MessageManager.getMessage("menu."+grant.getMenu(),locale))
                            .append("</a>");
                }
            }
            builder.append("</div>");
            builder.append("<div style=\"float: left; width: 30%;\" align=\"right\">");
            Object userName = session.getAttribute(Authentification.PARAM_NAME_NAME);
            if(Objects.nonNull(userName)){
                builder.append("<a class=\"mainmenubutton\" href=\"/do?command=")
                        .append(COMMAND_NAME_LOGOUT)
                        .append("\" style=\"margin-right: 0;\">")
                        .append(MessageManager.getMessage("main.logout",locale))
                        .append("</a>");
            } else {
                builder.append("<a class=\"mainmenubutton\" href=\"")
                        .append(ConfigurationManager.getProperty(PATH_PAGE_LOGIN))
                        .append("\" style=\"margin-right: 0;\">")
                        .append(MessageManager.getMessage("main.login",locale))
                        .append("</a>")
                        .append("<a class=\"mainmenubutton\" href=\"")
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