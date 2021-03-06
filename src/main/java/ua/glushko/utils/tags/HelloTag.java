package ua.glushko.utils.tags;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;

import static ua.glushko.commands.Command.*;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 * Print User hello on the page
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("serial")
public class HelloTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {
            HttpSession session = pageContext.getSession();
            String locale = (String) session.getAttribute(PARAM_LOCALE);
            String currentUserName = (String) pageContext.getSession().getAttribute(Authentication.PARAM_NAME_NAME);
            Timestamp currentUserLastLogin = (Timestamp) session.getAttribute(Authentication.PARAM_NAME_LAST_LOGIN);
            String PROPERTY_NAME_WELCOME = "app.welcome.message";
            String welcome = MessageManager.getMessage(PROPERTY_NAME_WELCOME,locale);
            String PROPERTY_NAME_LAST_LOGIN = "user.lastLogin";
            String lastLogin = MessageManager.getMessage(PROPERTY_NAME_LAST_LOGIN,locale);
            StringBuilder welcomeMessage = new StringBuilder(welcome);

            if(Objects.nonNull(currentUserName) && !currentUserName.isEmpty()){
                welcomeMessage.append(" ").append(escapeHtml(currentUserName)).append(" ");

                if(Objects.nonNull(currentUserLastLogin))
                            welcomeMessage.append(lastLogin).append(" ").append(currentUserLastLogin.toString());
            } else {
                        welcomeMessage.append(" Guest ");
            }

            pageContext.getOut().write(welcomeMessage.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}