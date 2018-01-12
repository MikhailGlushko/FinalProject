package ua.glushko.tags;

import ua.glushko.configaration.MessageManager;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;

import static ua.glushko.commands.AbstractCommand.*;

@SuppressWarnings("serial")
public class HelloTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {

            HttpSession session = pageContext.getSession();
            String locale = (String) session.getAttribute(PARAM_NAME_LOCALE);
            String userName = (String) session.getAttribute(PARAM_NAME_USER_NAME);
            Timestamp userLastLogin = (Timestamp) session.getAttribute(PARAM_NAME_USER_LAST_LOGIN);
            String PROPERTY_NAME_WELCOME = "app.welcome.message";
            String welcome = MessageManager.getMessage(PROPERTY_NAME_WELCOME,locale);
            String PROPERTY_NAME_LAST_LOGIN = "user.lastLogin";
            String lastLogin = MessageManager.getMessage(PROPERTY_NAME_LAST_LOGIN,locale);
            StringBuilder welcomeMessage = new StringBuilder(welcome);

            if(Objects.nonNull(userName) && !userName.isEmpty()){
                welcomeMessage.append(" ").append(userName).append(" ");

                if(Objects.nonNull(userLastLogin))
                            welcomeMessage.append(lastLogin).append(" ").append(userLastLogin.toString());
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