package ua.glushko.commands;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Route to new page
 */
public class CommandRouter {
    private final Logger LOGGER = Logger.getLogger(ICommand.class.getSimpleName());
    public static final boolean FORWARD = true;
    public static final boolean REDIRECT = false;

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String page;
    private final boolean isForward;

    public CommandRouter(HttpServletRequest request, HttpServletResponse response, String page, boolean isForward) {
        this.request = request;
        this.response = response;
        this.page = page;
        this.isForward = isForward;
    }

    public CommandRouter(HttpServletRequest request, HttpServletResponse response, String page) {
        this(request, response, page, true);
    }

    public void route() {
        try {
                if (isForward) {
                    request.getRequestDispatcher(page).forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + page);
                }
        } catch (ServletException | IOException e) {
            LOGGER.error(e);
        }
    }
}
