package ua.glushko.configaration;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationManagerTest {

    @Test
    public void getProperty() {
        String property = ConfigurationManager.getProperty("path.page.guestbook");
        assertNotNull(property);
    }

    @Test
    public void getNoExistProperty() {
        String property = ConfigurationManager.getProperty("path.page.guestbook1");
        assertEquals("path.page.guestbook1",property);
    }

}