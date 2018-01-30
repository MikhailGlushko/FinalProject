package ua.glushko.commands.impl.admin.users;

import ua.glushko.commands.utils.Validator;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class UsersCommandHelper {
    public static final String PATH_PAGE_USERS = "path.page.users";

    public static final String PARAM_NAME_USER    = "user_detail";
    static final String PARAM_USER_LIST = "users_list";
    static final String PARAM_USER_LIST_TITLE = "users_list_head";

    static final String PATH_PAGE_USERS_DETAIL = "path.page.usersdetail";
    public static final String PATH_PAGE_USERS_SETUP = "path.page.userssetup";
    static final String PATH_PAGE_USERS_ADD = "path.page.usersadd";

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_USER_LOGIN = "user_login";
    public static final String PARAM_USER_PASSWORD = "user_password";
    public static final String PARAM_USER_PASSWORD2 = "user_password2";
    public static final String PARAM_USER_SECRET = "user_secret";
    public static final String PARAM_USER_NAME = "user_name";
    public static final String PARAM_USER_ROLE = "user_role";
    public static final String PARAM_USER_STATUS = "user_status";
    public static final String PARAM_USER_EMAIL = "user_email";
    public static final String PARAM_USER_PHONE = "user_phone";

    public static final String MESSAGE_USER_STATUS = "user.status.";
    public static final String MESSAGE_USER_INCORRECT_LOGIN_OR_PASSWORD = "user.incorrectLoginOrPassword";
    public static final String MESSAGE_USER_IS_REGISTERED = "user.isRegistered";
    public static final String MESSAGE_USER_PASSWORD_WAS_SEND = "user.passwordSent";
    public static final String MESSAGE_USER_PASSWORD_WAS_CHANGED = "user.passwordChange";
    public static final String MESSAGE_USER_ALREADY_EXIST = "user.alreadyexist";
    public static final String MESSAGE_USER_INCORRECT_DATA = "user.incorrectdata";
    public static final String MESSAGE_USER_NOT_EXIST = "user.notexist";

    public static final String MESSAGE_USER_DATABASE_NOT_FOUND = "database.not.found";


    public static User getValidatedUserBeforeRecoveryPassword(HttpServletRequest request) throws ParameterException {
        String userLogin = request.getParameter(PARAM_USER_LOGIN);
        if (Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        User user = new User();
        user.setLogin(userLogin);
        return user;
    }

    /** Check User data before login */
    public static User getValidatedUserBeforeLogin(HttpServletRequest request) throws ParameterException {
        String userLogin = request.getParameter(PARAM_USER_LOGIN);
        String userPassword = request.getParameter(PARAM_USER_PASSWORD);
        if (Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if (Objects.isNull(userPassword) || userPassword.isEmpty())
            throw new ParameterException("user.password.not.resent");
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        return user;
    }

    /** *Check user data before register */
    public static User getValidatedUserBeforeRegistration(HttpServletRequest request) throws ParameterException {
        String userLogin = request.getParameter(PARAM_USER_LOGIN);
        String userPassword = request.getParameter(PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_USER_PASSWORD2);
        String userName  = request.getParameter(PARAM_USER_NAME);
        String userEmail = request.getParameter(PARAM_USER_EMAIL);
        String userPhone = request.getParameter(PARAM_USER_PHONE);
        if(Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if(Objects.isNull(userPassword) || userPassword.isEmpty())
            throw new ParameterException("user.password.not.present");
        if(Objects.isNull(userPassword2) || userPassword2.isEmpty() || !userPassword2.equals(userPassword))
            throw new ParameterException("user.password.not.match");
        if(Objects.isNull(userName) || userName.isEmpty())
            throw new ParameterException("user.name.not.present");
        if(Objects.isNull(userEmail) || userEmail.isEmpty())
            throw new ParameterException("user.email.not.present");
        if(Objects.isNull(userPhone) || userPhone.isEmpty())
            throw new ParameterException("user.phone.not.present");
        if(!Validator.validateLogin(userLogin))
            throw new ParameterException("user.login.incorrect");
        if(!Validator.validatePassword(userPassword))
            throw new ParameterException("user.password.incorrect");
        if(!Validator.validateEmail(userEmail))
            throw new ParameterException("user.email.incorrect");
        if(!Validator.validatePhone(userPhone))
            throw new ParameterException("user.phone.incorrect");
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        return user;
    }

    public static User getValidatedUserBeforeSetup(HttpServletRequest request) throws ParameterException {
        String userId = request.getParameter(PARAM_USER_ID);
        String userLogin = request.getParameter(PARAM_USER_LOGIN);
        String userPassword = request.getParameter(PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_USER_PASSWORD2);
        String userName  = request.getParameter(PARAM_USER_NAME);
        String userEmail = request.getParameter(PARAM_USER_EMAIL);
        String userPhone = request.getParameter(PARAM_USER_PHONE);
        if(Objects.isNull(userId) || userId.isEmpty())
            throw new ParameterException("user.id.not.present");
        if(Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if(Objects.isNull(userName) || userName.isEmpty())
            throw new ParameterException("user.name.not.present");
        if(Objects.isNull(userEmail) || userEmail.isEmpty())
            throw new ParameterException("user.email.not.present");
        if(Objects.isNull(userPhone) || userPhone.isEmpty())
            throw new ParameterException("user.phone.not.present");
        if(!Validator.validateEmail(userEmail))
            throw new ParameterException("user.email.incorrect");
        if(!Validator.validatePhone(userPhone))
            throw new ParameterException("user.phone.incorrect");
        if(Objects.nonNull(userPassword) && !userPassword.isEmpty()
                && Objects.nonNull(userPassword2) && !userPassword2.isEmpty()
                && !Validator.validatePassword(userPassword))
            throw new ParameterException("user.password.incorrect");
        User user = new User();
        user.setId(Integer.valueOf(userId));
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        return user;
    }

    public static User getValidatedUserBeforeCreate(HttpServletRequest request) throws ParameterException{
        String userLogin = request.getParameter(PARAM_USER_LOGIN);
        String userPassword = request.getParameter(PARAM_USER_PASSWORD);
        String userName = request.getParameter(PARAM_USER_NAME);
        String userEmail = request.getParameter(PARAM_USER_EMAIL);
        String userPhone = request.getParameter(PARAM_USER_PHONE);
        String userStatus = request.getParameter(PARAM_USER_STATUS);
        String userRole = request.getParameter(PARAM_USER_ROLE);
        if(Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if(Objects.isNull(userPassword) || userPassword.isEmpty())
            throw new ParameterException("user.password.not.present");
        if(Objects.isNull(userName) || userName.isEmpty())
            throw new ParameterException("user.name.not.present");
        if(Objects.isNull(userEmail) || userEmail.isEmpty())
            throw new ParameterException("user.email.not.present");
        if(Objects.isNull(userPhone) || userPhone.isEmpty())
            throw new ParameterException("user.phone.not.present");
        if(Objects.isNull(userStatus) || userStatus.isEmpty())
            throw new ParameterException("user.status.not.present");
        if(Objects.isNull(userRole) || userRole.isEmpty())
            throw new ParameterException("user.role.not.present");
        if(!Validator.validateLogin(userLogin))
            throw new ParameterException("user.login.incorrect");
        if(!Validator.validatePassword(userPassword))
            throw new ParameterException("user.password.incorrect");
        if(!Validator.validateEmail(userEmail))
            throw new ParameterException("user.email.incorrect");
        if(!Validator.validatePhone(userPhone))
            throw new ParameterException("user.phone.incorrect");
        if(!Validator.validateUserStatus(userStatus))
            throw new ParameterException("user.status.incorrect");
        if(!Validator.validateUserRole(userRole))
            throw new ParameterException("user.role.incorrect");
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        user.setStatus(userStatus);
        user.setRole(userRole);
        return user;
    }

    /** Check user data before password change */
    public static void getValidatedUserBeforePasswordChange(HttpServletRequest request) throws ParameterException{
        String userLogin     = request.getParameter(PARAM_USER_LOGIN);
        String userPassword  = request.getParameter(PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_USER_PASSWORD2);
        if(Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if(Objects.isNull(userPassword) || userPassword.isEmpty())
            throw new ParameterException("user.password.not.present");
        if(Objects.isNull(userPassword2) || userPassword2.isEmpty() || !userPassword2.equals(userPassword))
            throw new ParameterException("user.password.not.match");
        if(!Validator.validatePassword(userPassword))
            throw new ParameterException("user.password.incorrect");
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(userPassword);
    }

    /** Check User data before password reset */
    public static User getValidatedUserBeforeResetPassword(HttpServletRequest request) throws ParameterException{
        String userLogin     = request.getParameter(PARAM_USER_LOGIN);
        String userPassword  = request.getParameter(PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_USER_PASSWORD2);
        String userSecret    = request.getParameter(PARAM_USER_SECRET);
        String sessionId     = request.getSession().getId();
        if(Objects.isNull(sessionId) || sessionId.isEmpty())
            throw new ParameterException("user.session.expired");
        if(Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        if(Objects.isNull(userPassword) || userPassword.isEmpty())
            throw new ParameterException("user.password.not.present");
        if(Objects.isNull(userPassword2) || userPassword2.isEmpty() || !userPassword2.equals(userPassword))
            throw new ParameterException("user.password.not.match");
        if(Objects.isNull(userSecret) || userSecret.isEmpty() || !userSecret.equals(sessionId))
            throw new ParameterException("user.secret.key.not.match");
        if(!Validator.validatePassword(userPassword))
            throw new ParameterException("user.password.incorrect");
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        return user;
    }

    /** Check User data before update */
    public static User getValidatedUserBeforeUpdateDetails(HttpServletRequest request) throws ParameterException{
        String userId = request.getParameter(PARAM_USER_ID);
        String userName = request.getParameter(PARAM_USER_NAME);
        String userEmail = request.getParameter(PARAM_USER_EMAIL);
        String userPhone = request.getParameter(PARAM_USER_PHONE);
        String userStatus = request.getParameter(PARAM_USER_STATUS);
        String userRole = request.getParameter(PARAM_USER_ROLE);
        if(Objects.isNull(userId) || userId.isEmpty())
            throw new ParameterException("user.id.not.present");
        if(Objects.isNull(userName) || userName.isEmpty())
            throw new ParameterException("user.name.not.present");
        if(Objects.isNull(userEmail) || userEmail.isEmpty())
            throw new ParameterException("user.email.not.present");
        if(Objects.isNull(userPhone) || userPhone.isEmpty())
            throw new ParameterException("user.phone.not.present");
        if(Objects.isNull(userStatus) || userStatus.isEmpty())
            throw new ParameterException("user.status.not.present");
        if(Objects.isNull(userRole) || userRole.isEmpty())
            throw new ParameterException("user.role.not.present");
        if(!Validator.validateUserId(userId))
            throw new ParameterException("user.id.incorrect");
        if(!Validator.validateEmail(userEmail))
            throw new ParameterException("user.email.incorrect");
        if(!Validator.validatePhone(userPhone))
            throw new ParameterException("user.phone.incorrect");
        if(!Validator.validateUserStatus(userStatus))
            throw new ParameterException("user.status.incorrect");
        if(!Validator.validateUserRole(userRole))
            throw new ParameterException("user.role.incorrect");
        User user = new User();
        user.setId(Integer.valueOf(userId));
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        user.setStatus(userStatus);
        user.setRole(userRole);
        return user;
    }
}
