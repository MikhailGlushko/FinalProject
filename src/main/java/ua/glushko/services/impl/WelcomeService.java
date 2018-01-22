package ua.glushko.services.impl;

import ua.glushko.services.AbstractService;

public class WelcomeService extends AbstractService {

    private WelcomeService() {
    }

    public static WelcomeService getService() {
        return new WelcomeService();
    }

 }
