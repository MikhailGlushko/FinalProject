package ua.glushko.model.dao;

import ua.glushko.model.dao.impl.GrantDAO;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.dao.impl.RepairServiceDAO;
import ua.glushko.model.dao.impl.UserDAO;
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
}
