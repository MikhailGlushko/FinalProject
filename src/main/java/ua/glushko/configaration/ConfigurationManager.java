package ua.glushko.configaration;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private static final String FILE_NAME_CONFIG = "config";

    private static ResourceBundle resourceBundle;

    private ConfigurationManager() {
    }

    public static String getProperty(String key) {
        if (resourceBundle == null)
            resourceBundle = ResourceBundle.getBundle(FILE_NAME_CONFIG);
        return resourceBundle.getString(key);
    }
}
