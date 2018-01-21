package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GuestBookDAO;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.List;

public class WelcomeService extends AbstractService {

    private WelcomeService() {
    }

    public static WelcomeService getService() {
        return new WelcomeService();
    }

 }
