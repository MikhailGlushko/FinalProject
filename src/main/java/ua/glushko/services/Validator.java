package ua.glushko.services;

import java.util.Objects;

class Validator {
    public static boolean validateLogin(String login) {
        String pattern = "^[a-zA-Z]+([a-zA-Z0-9-._]){7,}";
        return Objects.nonNull(login) && login.matches(pattern);
    }

    public static boolean validatePassword(String password) {
        // what should be required and how many times
        String pattern1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#%^+-=_.,])(?=\\S+$).{8,}$";
        // what should be == what should not be
        String pattern2 = "[a-zA-Z0-9~!@#%^+-=_.,]+";
        //TODO there should be no repetition
        String pattern3 = "([.])\\1{2}";
        return (Objects.nonNull(password)) && password.matches(pattern2)
                && !password.matches(pattern3) && password.matches(pattern1);
    }
}
