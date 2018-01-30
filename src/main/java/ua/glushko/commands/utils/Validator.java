package ua.glushko.commands.utils;

import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.entity.UserStatus;

import java.util.Objects;

/**
 * Verify the requirement for user data
 * @author Mikhail Glushko
 * @version 1.0
 */
public class Validator {
    private static final boolean isLoginValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.login"));
    private static final boolean isPasswordValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.password"));
    private static final boolean isEmailValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.email"));
    private static final boolean isPhoneValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.phone"));

    /** Check userId*/
    public static boolean validateUserId(String id){
        try{
            //noinspection ResultOfMethodCallIgnored
            Integer.valueOf(id);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    /** Check userStatus */
    public static boolean validateUserStatus(String status){
        try {
            UserStatus.valueOf(status);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    /** *Check UserRole */
    public static boolean validateUserRole(String role){
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
        String pattern1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#%^+-=_.,])(?=\\S+$).{8,}$";
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
    public static boolean validatePhone(String phone){
        String pattern = "^\\+\\d{2}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$";
        return !isPhoneValidation  || (Objects.nonNull(phone) && phone.matches(pattern));
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
