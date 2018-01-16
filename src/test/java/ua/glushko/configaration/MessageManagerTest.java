package ua.glushko.configaration;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageManagerTest {

    @Test
    public void getMessage() {
        String message = MessageManager.getMessage("app.welcome.title");
        assertNotNull(message);
        message = MessageManager.getMessage("app.welcome.title1");
        assertEquals("app.welcome.title1",message);
    }

    @Test
    public void getMessageLocale() {
        String message = MessageManager.getMessage("app.welcome.title","ru");
        assertNotNull(message);
        MessageManager.getMessage("app.welcome.title","en");
        assertNotNull(message);
        message = MessageManager.getMessage("app.welcome.title1","ru");
        assertEquals("app.welcome.title1",message);

    }
}