package ua.glushko.services.utils;

import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.Scope;
import ua.glushko.model.entity.UserRole;
import ua.glushko.exception.ParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;

/**
 * Checking the rights of the current user
 */
public class Authentication {

    public static final String PARAM_ID = "id";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_NAME_NAME = "name";
    public static final String PARAM_ROLE = "role";
    public static final String PARAM_GRANTS = "grants";
    public static final String PARAM_NAME_LAST_LOGIN = "last_login";

    enum CRUD {
        M(0), C(128), R(64), U(32), D(16);
        private final int value;

        CRUD(int value) {
            this.value = value;
        }
    }

    public static final int C = 128;
    public static final int c = 8;
    public static final int R = 64;
    public static final int r = 4;
    public static final int U = 32;
    public static final int u = 2;
    public static final int D = 16;
    public static final int d = 1;

    private Authentication() {
    }

    /**
     * Checks if there is information about the user in the session
     */
    public static boolean isUserLogIn(HttpSession session) {
        return Objects.nonNull(session.getAttribute(Authentication.PARAM_LOGIN));
    }


    public static String getCurrentUserLogin(HttpSession session) {
        Object attribute = session.getAttribute(Authentication.PARAM_LOGIN);
        if (Objects.isNull(attribute))
            return null;
        return attribute.toString();
    }

    public static Integer getCurrentUserId(HttpSession session){
        try {
            return Integer.valueOf((String) session.getAttribute(Authentication.PARAM_ID));
        } catch (NumberFormatException e){}
        return null;
    }

    /**
     * Performs the calculation of the access level based on previously saved data in the session upon successful authorization of the user
     *
     * @see UserRole
     * @see Grant
     * @see Scope
     */
    public static int checkAccess(HttpServletRequest request) throws ParameterException {
        int userAccess = 0;
        List currentUserGrantList = null;
        if(request.getSession().getAttribute(PARAM_GRANTS) instanceof List)
            currentUserGrantList = (List) request.getSession().getAttribute(PARAM_GRANTS);
        if (Objects.isNull(currentUserGrantList))
            throw new ParameterException("grants.is.null");
        UserRole currentUserRole = (UserRole) request.getSession().getAttribute(PARAM_ROLE);
        if (Objects.isNull(currentUserRole))
            throw new ParameterException("role.is.null");
        String command = request.getParameter(PARAM_COMMAND);
        for (Object grant : currentUserGrantList) {
            if (grant instanceof Grant && currentUserRole == ((Grant)grant).getRole() && command.startsWith(((Grant)grant).getCommand())) // считаем доступ только для нужной нам комманды
                userAccess |= calculateAccess((Grant)grant);
        }

        LOGGER.debug(userAccess);
        return userAccess;
    }

    /**
     * Calculation of access for one command
     *
     * @param grant - Grants
     * @return - grants as byte value
     */
    private static int calculateAccess(Grant grant) {
        int userAccess = 0;
        String grantAction = grant.getAction();   // allowed action
        Scope grantScope = grant.getScope();      // scope
        char[] actionCharArray = grantAction.toCharArray();
        for (char oneChar : actionCharArray) {
            CRUD crud = CRUD.valueOf(String.valueOf(oneChar));
            switch (grantScope) {
                case ALL:
                    userAccess |= crud.value;
                case OWNER:
                    userAccess |= crud.value >> 4;
            }
        }
        return userAccess;
    }
}
