package ua.glushko.commands.utils;

import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.exception.ParameterException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class Validator {
    private static final boolean isLoginValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.login"));
    private static final boolean isPasswordValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.password"));
    private static final boolean isEmailValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.email"));
    private static final boolean isPhoneValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.phone"));

    /** Check userId*/
    static boolean validateUserId(String id){
        try{
            //noinspection ResultOfMethodCallIgnored
            Integer.valueOf(id);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    /** Check userStatus */
    static boolean validateUserStatus(String status){
        try {
            UserStatus.valueOf(status);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    /** *Check UserRole */
    static boolean validateUserRole(String role){
        try{
            UserRole.valueOf(role);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    /** Check UserLogin */
    public static boolean validateLogin(String login) {
        String pattern = "^[a-zA-Z]+([a-zA-Z0-9-._]){7,}";
        return Objects.nonNull(login) && (!isLoginValidation || login.matches(pattern));
    }

    /** Check UserPassword */
    public static boolean validatePassword(String password) {
        // what should be required and how many times
        String pattern1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#%^+-=_.,])(?=\\S+$).{8,}$";
        // what should be == what should not be
        String pattern2 = "[a-zA-Z0-9~!@#%^+-=_.,]+";
        String pattern3 = "([.])\\1{2}";
        return (Objects.nonNull(password)) && (!isPasswordValidation || password.matches(pattern2)
                && !password.matches(pattern3) && password.matches(pattern1));
    }

    /** Check UserEmail */
    public static boolean validateEmail(String email){
        String pattern = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        return Objects.nonNull(email) && (!isEmailValidation || email.matches(pattern));
    }

    /** Check UserPhone */
    private static boolean validatePhone(String phone){
        String pattern = "^\\+\\d{2}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$";
        return !isPhoneValidation  || (Objects.nonNull(phone) && phone.matches(pattern));
    }

    public static User getValidatedUserBeforeRecoveryPassword(HttpServletRequest request) throws ParameterException{
        String userLogin = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        if (Objects.isNull(userLogin) || userLogin.isEmpty())
            throw new ParameterException("user.login.not.present");
        User user = new User();
        user.setLogin(userLogin);
        return user;
    }

    /** Check User data before login */
    public static User getValidatedUserBeforeLogin(HttpServletRequest request) throws ParameterException {
        String userLogin = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
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
        String userLogin = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2);
        String userName  = request.getParameter(UsersCommandHelper.PARAM_USER_NAME);
        String userEmail = request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL);
        String userPhone = request.getParameter(UsersCommandHelper.PARAM_USER_PHONE);
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
        String userId = request.getParameter(UsersCommandHelper.PARAM_USER_ID);
        String userLogin = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2);
        String userName  = request.getParameter(UsersCommandHelper.PARAM_USER_NAME);
        String userEmail = request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL);
        String userPhone = request.getParameter(UsersCommandHelper.PARAM_USER_PHONE);
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
        String userLogin = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
        String userName = request.getParameter(UsersCommandHelper.PARAM_USER_NAME);
        String userEmail = request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL);
        String userPhone = request.getParameter(UsersCommandHelper.PARAM_USER_PHONE);
        String userStatus = request.getParameter(UsersCommandHelper.PARAM_USER_STATUS);
        String userRole = request.getParameter(UsersCommandHelper.PARAM_USER_ROLE);
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
        if(!validateUserStatus(userStatus))
            throw new ParameterException("user.status.incorrect");
        if(!validateUserRole(userRole))
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
        String userLogin     = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword  = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2);
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
        String userLogin     = request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN);
        String userPassword  = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD);
        String userPassword2 = request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2);
        String userSecret    = request.getParameter(UsersCommandHelper.PARAM_USER_SECRET);
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
        String userId = request.getParameter(UsersCommandHelper.PARAM_USER_ID);
        String userName = request.getParameter(UsersCommandHelper.PARAM_USER_NAME);
        String userEmail = request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL);
        String userPhone = request.getParameter(UsersCommandHelper.PARAM_USER_PHONE);
        String userStatus = request.getParameter(UsersCommandHelper.PARAM_USER_STATUS);
        String userRole = request.getParameter(UsersCommandHelper.PARAM_USER_ROLE);
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
        if(!validateUserId(userId))
            throw new ParameterException("user.id.incorrect");
        if(!Validator.validateEmail(userEmail))
            throw new ParameterException("user.email.incorrect");
        if(!Validator.validatePhone(userPhone))
            throw new ParameterException("user.phone.incorrect");
        if(!validateUserStatus(userStatus))
            throw new ParameterException("user.status.incorrect");
        if(!validateUserRole(userRole))
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

    /** Check userStatus is ACTIVE*/
    public static boolean isUserStatusNotActive(User currentUser) {
        return currentUser != null && currentUser.getStatus() != UserStatus.ACTIVE;
    }

    /** Check UserStatus is not ACTIVE*/
    public static boolean isUserStatusActive(User currentUser) {
        return currentUser != null && currentUser.getStatus() == UserStatus.ACTIVE;
    }

}
