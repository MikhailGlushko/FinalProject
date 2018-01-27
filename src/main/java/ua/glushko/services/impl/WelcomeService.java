package ua.glushko.services.impl;

import ua.glushko.services.Service;

public class WelcomeService extends Service {

    private WelcomeService() {
    }

    public static WelcomeService getService() {
        return new WelcomeService();
    }

 }
