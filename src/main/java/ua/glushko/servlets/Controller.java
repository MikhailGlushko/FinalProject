package ua.glushko.servlets;

import ua.glushko.commands.GenericCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.CommandFactory;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.transaction.MySQLDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/do")
public class Controller extends HttpServlet {

    private final String PROPERTY_NAME_DATABASE = "database";
    private final String DATABASE_NAME = ConfigurationManager.getProperty(PROPERTY_NAME_DATABASE);

    @Override
    public void init() throws ServletException {
        super.init();
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            switch (DATABASE_NAME) {
                case "H2":
                    ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
                    break;
                case "MYSQL":
                    ConnectionPool.getConnectionPool().setDataSource(MySQLDataSource.getDatasource());
                    break;
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

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        CommandFactory commandFactory = CommandFactory.getInstance();
        // get command
        GenericCommand command = commandFactory.getCommand(req);
        // execute command and get page to route
        CommandRouter commandRouter = command.execute(req, resp);
        // route to new page
        commandRouter.route();
    }
}
