package ua.glushko.configaration;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {
    private static final String FILE_NAME_MESSAGES = "messages";
    private static final String VALUE_LOCALE_RU = "ru";
    private static final String VALUE_LOCALE_EN = "en";

    private static ResourceBundle resourceBundle;

    private MessageManager() {
    }

    public static String getMessage(String key) {
        if (resourceBundle == null)
            resourceBundle = ResourceBundle.getBundle(FILE_NAME_MESSAGES);
        return resourceBundle.getString(key);
    }

    public static String getMessage(String key, String locale) {
        Locale loc;
        if (locale == null || locale.equals(VALUE_LOCALE_EN))
            loc = Locale.getDefault();
        else
            loc = new Locale(VALUE_LOCALE_RU);
        resourceBundle = ResourceBundle.getBundle(FILE_NAME_MESSAGES, loc);
        String result;
        try {
            result = resourceBundle.getString(key);
        } catch (MissingResourceException e){
            result=key;
        }
        return result;
    }
}
