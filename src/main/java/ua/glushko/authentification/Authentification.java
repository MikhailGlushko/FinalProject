package ua.glushko.authentification;

import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.Scope;
import ua.glushko.model.entity.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;

/**
 * Checking the rights of the current user
 */
public class Authentification {

    public static final String PARAM_NAME_ID = "id";
    public static final String PARAM_NAME_LOGIN = "login";
    public static final String PARAM_NAME_NAME = "name";
    public static final String PARAM_NAME_ROLE = "role";
    public static final String PARAM_NAME_GRANTS = "grants";
    public static final String PARAM_NAME_LAST_LOGIN = "last_login";

    enum CRUD {
        M(0), C(128), R(64), U(32), D(16);
        private int value;

        CRUD(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
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

    private Authentification() {
    }

    /**
     * Checks if there is information about the user in the session
     */
    public static boolean isUserLogIn(HttpSession session) {
        return Objects.nonNull(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN));
    }

    /**
     * Performs the calculation of the access level based on previously saved data in the session upon successful authorization of the user
     * @see UserRole
     * @see Grant
     * @see Scope
     */
    public static int checkAccess(HttpServletRequest request) {
        int userAccess = 0;
        try {
            List<Grant> currentUserGrantList = (List<Grant>) request.getSession().getAttribute(PARAM_NAME_GRANTS);
            UserRole currentUserRole = (UserRole) request.getSession().getAttribute(PARAM_NAME_ROLE);
            String command = request.getParameter(PARAM_NAME_COMMAND);
            for (Grant grant : currentUserGrantList) {
                if (currentUserRole == grant.getRole() && command.startsWith(grant.getCommand())) // считаем доступ только для нужной нам комманды
                    userAccess |= calculateAccess(grant);
            }
        } catch (NullPointerException | NumberFormatException | ClassCastException e) {
            LOGGER.error(e);
        }
        LOGGER.debug(userAccess);
        return userAccess;
    }

    /**
     * Calculation of access for one command
     * @param grant
     * @return
     */
    private static int calculateAccess(Grant grant) {
        int userAccess = 0;
        String grantActionon = grant.getAction();   // allowed action
        Scope grantScopepe = grant.getScope();      // scope
        char[] actionCharArray = grantActionon.toCharArray();
        for (char oneChar : actionCharArray) {
            CRUD crud = CRUD.valueOf(String.valueOf(oneChar));
            switch (grantScopepe) {
                case ALL:
                    userAccess |= crud.value;
                case OWNER:
                    userAccess |= crud.value >> 4;
            }
        }
        return userAccess;
    }
}
