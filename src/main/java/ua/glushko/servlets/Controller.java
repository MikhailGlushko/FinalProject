package ua.glushko.servlets;

import org.apache.log4j.Logger;
import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.CommandFactory;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.transaction.ConnectionPool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
/**
 * Main Controller
 * @author Mikhail Glushko
 * @version 1.0
 */
@WebServlet(PARAM_SERVLET_PATH)
public class Controller extends HttpServlet {
    Logger LOGGER = Logger.getLogger(Command.class.getSimpleName());
    private final String DATA_SOURCE = ConfigurationManager.getProperty("jdbc/repair_agency");
    private final String JNDI_NAME   = ConfigurationManager.getProperty("java:/comp/env");

    @Override
    public void init() throws ServletException {
        super.init();
        if (ConnectionPool.getConnectionPool().getDataSource() == null){
            try {
                Context context = (Context)(new InitialContext().lookup(JNDI_NAME));
                DataSource dataSource = (DataSource) context.lookup(DATA_SOURCE);
                ConnectionPool.getConnectionPool().setDataSource(dataSource);
            } catch (NamingException e) {
                LOGGER.error(e);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        CommandFactory commandFactory = CommandFactory.getInstance();
        Command command = commandFactory.getCommand(req);
        CommandRouter commandRouter = command.execute(req, resp);
        commandRouter.route();
    }
}
