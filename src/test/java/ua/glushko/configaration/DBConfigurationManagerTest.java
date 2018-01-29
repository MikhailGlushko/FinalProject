package ua.glushko.configaration;

import org.junit.Test;

import java.util.MissingResourceException;

import static org.junit.Assert.*;

public class DBConfigurationManagerTest {

    @Test
    public void getProperty() {
        String dbHost = DBConfigurationManager.getProperty("db_host");
        assertNotNull(dbHost);
    }

    @Test (expected = MissingResourceException.class)
    public void getNoExistProperty() {
        String dbHost = DBConfigurationManager.getProperty("db_host1");
        assertNull(dbHost);
    }
}