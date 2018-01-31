package ua.glushko.filters;

import ua.glushko.commands.utils.Authentication;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PATH_PAGE_LOGIN;
import static ua.glushko.commands.CommandFactory.*;

/**
 * Filter to To verify user authorization before calling commands
 * @author Mikhail Glushko
 * @version 1.0
 */
@WebFilter(urlPatterns = { PARAM_SERVLET_PATH})
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(PARAM_COMMAND);
        if(Objects.nonNull(command) && (command.equals(COMMAND_LOGIN) ||
                                        command.equals(COMMAND_LOGOUT)||
                                        command.equals(COMMAND_WELCOME) ||
                                        command.equals(COMMAND_LANG) ||
                                        command.equals(COMMAND_REGISTER)) ||
                                        command.equals(COMMAND_RESET_PASSWORD))
            filterChain.doFilter(servletRequest,servletResponse);
        else if (Authentication.isUserLogIn(((HttpServletRequest)servletRequest).getSession()))
                filterChain.doFilter(servletRequest,servletResponse);
        else
        ((HttpServletResponse)servletResponse).sendRedirect(ConfigurationManager.getProperty(PATH_PAGE_LOGIN));
    }

    @Override
    public void destroy() {
    }
}
