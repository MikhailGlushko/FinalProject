package ua.glushko.commands.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.CommandFactory;
import ua.glushko.servlets.Controller;
import ua.glushko.transaction.ConnectionPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_LOCALE;
import ua.glushko.transaction.H2DataSource;

public class SwitchLanguageCommandTest {
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response=mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void setUp(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(PARAM_LOCALE)).thenReturn("ru");
        when(request.getParameter(PARAM_COMMAND)).thenReturn(CommandFactory.COMMAND_LANG);
        when(request.getHeader(SwitchLanguageCommand.REFERRER)).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void switchToRu() throws ServletException {
        when(request.getParameter(PARAM_LOCALE)).thenReturn("ru");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }

    @Test
    public void switchToEn() throws ServletException {
        when(request.getParameter(PARAM_LOCALE)).thenReturn("en");
        Controller controller = new Controller();
        controller.init();
        controller.processRequest(request,response);
    }
}