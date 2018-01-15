package ua.glushko.services.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.entity.GenericEntity;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.util.List;

public class OrdersService extends AbstractService {

    private OrdersService() {
    }

    public static OrdersService getService() {
        return new OrdersService();
    }

    public List<Order> getOrderList() throws PersistException, TransactionException {
        return (List<Order>) getList(MySQLDAOFactory.getFactory().getOrderDao());
    }

    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<Order>) getList(MySQLDAOFactory.getFactory().getOrderDao(),page,pagesCount,rowsPerPage);
    }

    public List<String> getOrderTitles() {
        return  MySQLDAOFactory.getFactory().getOrderDao().getTableHead();
    }

    public Order getOrderById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getOrderDao(),id);
    }

    public void updateOrder(Order item) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getOrderDao(),item);
    }

    public void deleteOrder(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getOrderDao(),serviceId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderDao());
    }

    public int count(int id) throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderDao(),id);
    }

    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage, Integer id) throws PersistException, TransactionException {
        OrderDAO orderDAO = MySQLDAOFactory.getFactory().getOrderDao();

        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<? extends GenericEntity> read;
        try {
            TransactionManager.beginTransaction();
            read = orderDAO.read(start, limit,id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return (List<Order>)read;
    }
}
