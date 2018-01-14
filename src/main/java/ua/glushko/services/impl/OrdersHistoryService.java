package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;

import java.util.List;

public class OrdersHistoryService extends AbstractService {

    private OrdersHistoryService() {
    }

    public static OrdersHistoryService getService() {
        return new OrdersHistoryService();
    }

    public List<OrderHistory> getOrderHistoryList() throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO());
    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),page,pagesCount,rowsPerPage);
    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage, int userId) throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),page,pagesCount,rowsPerPage,userId);
    }

    public List<String> getOrderHistoryTitles() {
        return  MySQLDAOFactory.getFactory().getOrderHistoryDAO().getTableHead();
    }

    public OrderHistory getOrderHistoryById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),id);
    }

    public void updateOrderHistoty(OrderHistory item) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),item);
    }

    public void deleteOrder(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),serviceId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderHistoryDAO());
    }
}
