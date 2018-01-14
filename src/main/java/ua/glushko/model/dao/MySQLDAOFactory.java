package ua.glushko.model.dao;

import ua.glushko.model.dao.impl.*;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.entity.RepairService;

/**
 * Фабрика для получения экзепляров DAO
 * Использован шаблонабстрактная фабрика
 * Экземпляры DAO хранятся в HashMap
 */
public class MySQLDAOFactory {

    private static final MySQLDAOFactory factory = new MySQLDAOFactory();

    private MySQLDAOFactory() {
    }

    public static MySQLDAOFactory getFactory() {
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

    public OrderHistoryDAO getOrderHistoryDAO() {
        return OrderHistoryDAO.getInstance();
    }

    public GuestBookDAO getGuestBookDAO(){
        return GuestBookDAO.getInstance();
    }
}
