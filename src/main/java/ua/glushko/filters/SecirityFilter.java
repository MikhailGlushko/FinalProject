package ua.glushko.filters;

import ua.glushko.authentification.Authentification;
import ua.glushko.configaration.SecurityConfiguraton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

//@WebFilter(urlPatterns = { "/*"})
class SecirityFilter implements Filter {
    private static final String PARAM_NAME_COMMAND = "ucommand";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        SecurityConfiguraton configuraton = SecurityConfiguraton.getInstance();
        String requestURI = request.getRequestURI();
        String command = getStringCommand(request.getRequestURI(), configuraton.getEndPoints());
        String role = configuraton.security(command);
        if ("ALL".equals(role)) {
            request.setAttribute(PARAM_NAME_COMMAND, command);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        if (("AUTH".equals(role))
                && Authentification.isUserLogIn(request.getSession())) {
            request.setAttribute(PARAM_NAME_COMMAND, command);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        if (("AUTH".equals(role)
                && !Authentification.isUserLogIn(request.getSession()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void destroy() {
    }

    private String getStringCommand(String URI, Set<String> endPoints) {
        int dot = URI.indexOf(".");
        if (dot > -1 && dot < URI.length())
            URI = URI.substring(0, dot);
        for (String endPoint : endPoints) {
            //
            if (endPoint.contains(URI.substring(1)))
                return endPoint;
        }
        return null;
    }
}
