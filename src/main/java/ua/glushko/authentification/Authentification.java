package ua.glushko.authentification;

import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.Scope;
import ua.glushko.model.entity.UserRole;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.AbstractCommand.*;

public class Authentification {

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

    public static boolean isUserLogIn(HttpSession session) {
        return Objects.nonNull(session.getAttribute(PARAM_NAME_USER_LOGIN));
    }

    //public static int checkAccess(HttpServletRequest request){
    public static int checkAccess(List<Grant> grantList, UserRole userRole, String command){
        int access = 0;
        //HttpSession session = request.getSession();
        try{
            //List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
            //UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
            //String command = request.getParameter(PARAM_NAME_COMMAND);
            for (Grant grant : grantList) {
                UserRole grantRole = grant.getRole();           // роль
                String grantCommand = grant.getCommand();       // комманда
                String grantActionon = grant.getAction();       // допустимые действия
                Scope grantScopepe = grant.getScope();          // диапазон
                if (userRole == grantRole && command.startsWith(grantCommand)) {
                    for (CRUD value : CRUD.values()) {
                        switch (value) {
                            case C:
                                if (grantActionon.contains(value.name())) {
                                    if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 8;
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 128;
                                }
                                break;
                            case R:
                                if (grantActionon.contains(value.name())) {
                                    if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 4;
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 64;
                                }
                                break;
                            case U:
                                if (grantActionon.contains(value.name())) {
                                    if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 2;
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 32;
                                }
                                break;
                            case D:
                                if (grantActionon.contains(value.name())) {
                                    if ("OWNER".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 1;
                                    if ("ALL".equals(grantScopepe.toString().toUpperCase()))
                                        access |= 16;
                                }
                                break;
                        }
                    }
                }
            }
        } catch (NullPointerException | NumberFormatException e){
            LOGGER.error(e);
        }
        LOGGER.debug(access);
        return access;
    }
}
