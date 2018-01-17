package ua.glushko.services;

import ua.glushko.configaration.ConfigurationManager;

import java.text.ParseException;
import java.util.Objects;

public class Validator {
    static final boolean isLoginValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.login"));
    static final boolean isPasswordValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.password"));
    static final boolean isEmailValidation = Boolean.valueOf(ConfigurationManager.getProperty("valodation.email"));
    static final boolean isPhoneValidation = Boolean.valueOf(ConfigurationManager.getProperty("validation.phone"));

    public static boolean validateLogin(String login) {
        String pattern = "^[a-zA-Z]+([a-zA-Z0-9-._]){7,}";
        return Objects.nonNull(login) && (!isEmailValidation || login.matches(pattern));
    }

    public static boolean validatePassword(String password) {
        // what should be required and how many times
        String pattern1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#%^+-=_.,])(?=\\S+$).{8,}$";
        // what should be == what should not be
        String pattern2 = "[a-zA-Z0-9~!@#%^+-=_.,]+";
        //TODO there should be no repetition
        String pattern3 = "([.])\\1{2}";
        return (Objects.nonNull(password)) && (!isPasswordValidation || password.matches(pattern2)
                && !password.matches(pattern3) && password.matches(pattern1));
    }

    public static boolean validateEmail(String email){
        String pattern = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        return Objects.nonNull(email) && (!isEmailValidation || email.matches(pattern));
    }

    public static boolean validatePhone(String phone){
        String pattern = "^\\+\\d{2}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$";
        return Objects.nonNull(phone) && (!isPhoneValidation || phone.matches(pattern));
    }
}
