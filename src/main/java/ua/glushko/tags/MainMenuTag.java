package ua.glushko.tags;

import ua.glushko.authentification.Authentication;
import ua.glushko.commands.CommandFactory;
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
                    .append("<a class=\"btn btn-default btn-sm\" href=\"")
                    .append("/do?command="+ CommandFactory.COMMAND_WELCOME)
                    .append("\">")
                    .append(MessageManager.getMessage("main.home",locale))
                    .append("</a>");

            Object attributeUserGrants = session.getAttribute(Authentication.PARAM_GRANTS);

            List grants = null;
            if(Objects.nonNull(attributeUserGrants))
                grants = (List) attributeUserGrants;

            if(Objects.nonNull(grants))
            for (Object grant:grants) {
                if (grant instanceof Grant && ((Grant) grant).getAction().contains("M")){
                    builder.append("<a class=\"btn btn-default btn-sm\" href=\"/do?command=")
                            .append(((Grant)grant).getCommand())
                            .append("\">")
                            .append(MessageManager.getMessage("menu."+((Grant)grant).getMenu(),locale))
                            .append("</a>");
                }
            }
            builder.append("</div>");
            builder.append("<div style=\"float: left; width: 25%;\" align=\"right\">");
            Object userName = pageContext.getSession().getAttribute(Authentication.PARAM_NAME_NAME);
            if(Objects.nonNull(userName)){
                builder.append("<a class=\"btn btn-default btn-sm\" href=\"/do?command=")
                        .append(COMMAND_LOGOUT)
                        .append("\" style=\"margin-right: 20; float: right\">")
                        .append(MessageManager.getMessage("main.logout",locale))
                        .append("</a>");
            } else {
                builder.append("<a class=\"btn btn-default btn-sm\" href=\"")
                        .append(ConfigurationManager.getProperty(PATH_PAGE_LOGIN))
                        .append("\" style=\"margin-right: 20; float: right\">")
                        .append(MessageManager.getMessage("main.login",locale))
                        .append("</a>")
                        .append("<a class=\"btn btn-default btn-sm\" href=\"")
                        .append(ConfigurationManager.getProperty(PATH_PAGE_REGISTER))
                        .append("\" style=\"margin-right: 20; float: right\">")
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