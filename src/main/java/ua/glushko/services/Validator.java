package ua.glushko.services;

import java.util.Objects;

class Validator {
    public static boolean validateLogin(String login) {
        String pattern = "^[a-zA-Z]+([a-zA-Z0-9-._]){7,}";
        return Objects.nonNull(login) && login.matches(pattern);
    }

    public static boolean validatePassword(String password) {
        // что должно быть объязательно и сколько раз
        String pattern1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#%^+-=_.,])(?=\\S+$).{8,}$";
        // что должно быть == чего не должно быть
        String pattern2 = "[a-zA-Z0-9~!@#%^+-=_.,]+";
        //TODO не должно быть повторов
        String pattern3 = "([.])\\1{2}";
        return (Objects.nonNull(password)) && password.matches(pattern2)
                && !password.matches(pattern3) && password.matches(pattern1);
    }
}
