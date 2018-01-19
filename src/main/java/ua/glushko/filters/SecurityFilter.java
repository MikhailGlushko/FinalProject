package ua.glushko.filters;

import ua.glushko.authentification.Authentification;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.ParameterException;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

//@WebFilter(urlPatterns = { "/*"})
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(Authentification.PARAM_LOGIN);
        String login = null;
        if(Objects.nonNull(attribute))
            login = (String)attribute;

        //Object attribute = servletRequest.getAttribute(Authentification.PARAM_NAME_NAME);
        if(Objects.isNull(attribute) && Objects.nonNull(login)){
            UsersService usersService = UsersService.getService();
            User currentUser = null;
            try {
                currentUser = usersService.getUserByLogin(login);
            } catch (PersistException | TransactionException | ParameterException e) {
            }
            if(Objects.nonNull(currentUser)){
                request.setAttribute(Authentification.PARAM_NAME_NAME, currentUser.getName());
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
