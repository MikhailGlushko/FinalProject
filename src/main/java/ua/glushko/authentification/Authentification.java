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
/** Проверка прав текущего пользователя*/
public class Authentification {

    public static final String PARAM_NAME_ID  = "id";
    public static final String PARAM_NAME_LOGIN  = "login";
    public static final String PARAM_NAME_NAME   = "name";
    public static final String PARAM_NAME_ROLE   = "role";
    public static final String PARAM_NAME_GRANTS = "grants";
    public static final String PARAM_NAME_LAST_LOGIN = "last_login";
    //public static final String PARAM_NAME_PASSWORD  = "password";
    //public static final String PARAM_NAME_STATUS = "status";
    //public static final String PARAM_NAME_EMAIL = "email";
    //public static final String PARAM_NAME_PHONE = "phone";

    enum CRUD{C,R,U,D}
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

    /** Проверяет есть ли онформация о пользователе в сессии*/
    public static boolean isUserLogIn(HttpSession session) {
        return Objects.nonNull(session.getAttribute(UsersCommandHelper.PARAM_NAME_USER_LOGIN));
    }

    /** Выполняет расчет уровня доступа на основании ранее сохраненных в сессию данных при успешной авторизации пользователя
     * @see UserRole
     * @see Grant
     * @see Scope
     * */
    public static int checkAccess(HttpServletRequest request){
        int userAccess = 0; // no access
        try{
            //noinspection unchecked
            List<Grant> currentUserGrantList = (List<Grant>) request.getSession().getAttribute(PARAM_NAME_GRANTS);
            UserRole currentUserRole = (UserRole) request.getSession().getAttribute(PARAM_NAME_ROLE);
            String command = request.getParameter(PARAM_NAME_COMMAND);
            for (Grant grant : currentUserGrantList) {
                UserRole   grantRole = grant.getRole();     // роль
                String  grantCommand = grant.getCommand();  // комманда
                String grantActionon = grant.getAction();   // допустимые действия
                Scope   grantScopepe = grant.getScope();    // диапазон
                if (currentUserRole == grantRole && command.startsWith(grantCommand))
                    for (CRUD value : CRUD.values())
                        switch (value) {
                            case C:
                                if (grantActionon.contains(value.name()))
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 128|8; // ALL + OWNER
                                    else if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 8;    // ALL + OWNER
                                break;
                            case R:
                                if (grantActionon.contains(value.name()))
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 64|4; // ALL + OWNER
                                    else if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 4;    // ALL + OWNER
                                break;
                            case U:
                                if (grantActionon.contains(value.name()))
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 32|2; // ALL + OWNER
                                    else if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 2;    // ALL + OWNER
                                break;
                            case D:
                                if (grantActionon.contains(value.name()))
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 16|1; // ALL + OWNER
                                    else if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        userAccess |= 1;    // ALL + OWNER
                                break;
                        }
            }
        } catch (NullPointerException | NumberFormatException |ClassCastException e){
            LOGGER.error(e);
        }
        LOGGER.debug(userAccess);
        return userAccess;
    }
}
