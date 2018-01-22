package ua.glushko.servlets;

import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.auth.RecoveryCommand;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@WebServlet("/MailServlet")
public class MailServlet extends HttpServlet{

    private final Properties properties = new Properties();

    @Override
    public void init() {
        String PARAM_NAME_SMTP_HOST = "mail.smtp.host";
        String PARAM_NAME_SMTP_PORT = "mail.smtp.port";
        String PARAM_NAME_SMTP_USER = "mail.user.name";
        String PARAM_NAME_SMTP_PASS = "mail.user.password";
        properties.put(PARAM_NAME_SMTP_HOST, ConfigurationManager.getProperty(PARAM_NAME_SMTP_HOST));
        properties.put(PARAM_NAME_SMTP_PORT, ConfigurationManager.getProperty(PARAM_NAME_SMTP_PORT));
        properties.put(PARAM_NAME_SMTP_USER,ConfigurationManager.getProperty(PARAM_NAME_SMTP_USER));
        properties.put(PARAM_NAME_SMTP_PASS,ConfigurationManager.getProperty(PARAM_NAME_SMTP_PASS));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute(RecoveryCommand.PARAM_MAIL_SETUP,properties);
        RecoveryCommand recoveryCommand = new RecoveryCommand();
        CommandRouter commandRouter = recoveryCommand.execute(req, resp);
        commandRouter.route();
    }
}
