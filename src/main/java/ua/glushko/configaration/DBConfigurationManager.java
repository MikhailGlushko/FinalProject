package ua.glushko.configaration;

import java.util.ResourceBundle;

/**
 * Get Database Configuration Data
 * @author Mikhail Glushko
 * @version 1.0
 */
public class DBConfigurationManager {
    private static final String FILE_NAME_DB = "db";

    private static ResourceBundle resourceBundle;

    private DBConfigurationManager() {
    }

    public static String getProperty(String key) {
        if (resourceBundle == null)
            resourceBundle = ResourceBundle.getBundle(FILE_NAME_DB);
        return resourceBundle.getString(key);
    }
}
