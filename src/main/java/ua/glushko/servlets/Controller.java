package ua.glushko.servlets;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.CommandFactory;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.transaction.MySQLDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/do")
public class Controller extends HttpServlet {

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
                e.printStackTrace();
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

    //TODO private
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        CommandFactory commandFactory = CommandFactory.getInstance();
        // get command
        Command command = commandFactory.getCommand(req);
        // execute command and get page to route
        CommandRouter commandRouter = command.execute(req, resp);
        // route to new page
        commandRouter.route();
    }
}
