package ua.glushko.filters;

import ua.glushko.authentification.Authentication;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebFilter(urlPatterns = { "/do"})
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!Authentication.isUserLogIn(((HttpServletRequest)servletRequest).getSession()))
            ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
