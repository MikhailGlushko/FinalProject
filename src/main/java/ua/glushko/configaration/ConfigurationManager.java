package ua.glushko.configaration;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Get Application Configuration Data
 * @author Mikhail Glushko
 * @version 1.0
 */
public class ConfigurationManager {
    private static final String FILE_NAME_CONFIG = "config";

    private static ResourceBundle resourceBundle;

    private ConfigurationManager() {
    }

    public static String getProperty(String key) {
        if (resourceBundle == null)
            resourceBundle = ResourceBundle.getBundle(FILE_NAME_CONFIG);
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e){
            return key;
        }
    }
}
