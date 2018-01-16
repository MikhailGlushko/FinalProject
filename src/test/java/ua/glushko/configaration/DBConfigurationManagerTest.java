package ua.glushko.configaration;

import org.junit.Test;

import java.util.MissingResourceException;

import static org.junit.Assert.*;

public class DBConfigurationManagerTest {

    @Test
    public void getProperty() {
        String db_host = DBConfigurationManager.getProperty("db_host");
        assertNotNull(db_host);
    }

    @Test (expected = MissingResourceException.class)
    public void getNoExistProperty() {
        String db_host = DBConfigurationManager.getProperty("db_host1");
    }
}