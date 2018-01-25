package ua.glushko.model.dao;

import ua.glushko.model.dao.impl.*;

/**
 * DAO Factory
 */
public class DAOFactory {

    private static final DAOFactory factory = new DAOFactory();

    private DAOFactory() {
    }

    public static DAOFactory getFactory() {
        return factory;
    }

    public UserDAO getUserDao(){
        return UserDAO.getInstance();
    }

    public GrantDAO getGrantDao(){
        return GrantDAO.getInstance();
    }

    public RepairServiceDAO getRepairServiceDao() {
        return RepairServiceDAO.getInstance();
    }

    public OrderDAO getOrderDao() {
        return OrderDAO.getInstance();
    }

    public GuestBookDAO getGuestBookDAO(){
        return GuestBookDAO.getInstance();
    }

    public NewsDAO getNewsDAO(){
        return NewsDAO.getInstance();
    }

    public OrderQueDAO getOrderQueDao(){
        return OrderQueDAO.getInstance();
    }
}
