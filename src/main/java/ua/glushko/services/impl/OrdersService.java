package ua.glushko.services.impl;

import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.entity.*;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_PAGES_COUNT;
import static ua.glushko.commands.Command.PROPERTY_NAME_BROWSER_ROWS_COUNT;

/**
 * Order services
 * @author Mikhail Glushko
 * @version 1.0
 */
public class OrdersService extends Service {

    private OrdersService() {
    }

    public static OrdersService getService() {
        return new OrdersService();
    }

    /**
     * List of Orders
     */
    public List<Order> getOrderList() throws SQLException {
        return DAOFactory.getFactory().getOrderDao().read();
    }

    /**
     * List of Orders by limit
     */
    public List<Order> getOrderList(int page) throws SQLException {
        int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        int rowsPerPage = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        return DAOFactory.getFactory().getOrderDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /**
     * List of field names
     */
    public List<String> getOrderTitles() {
        return DAOFactory.getFactory().getOrderDao().getTableHead();
    }

    /**
     * Get order by id
     */
    public Order getOrderById(int id) throws SQLException {
        return getById(DAOFactory.getFactory().getOrderDao(), id);
    }

    /**
     * Delete exist order
     */
    public void deleteOrder(Integer serviceId) throws TransactionException, SQLException {
        delete(DAOFactory.getFactory().getOrderDao(), serviceId);
    }

    /**
     * Total of Orders
     */
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getOrderDao());
    }

    /**
     * Total of Orders
     */
    public int count(int id) throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getOrderDao(), id);
    }

    /**
     * List of Orders for userId with Limit
     */
    public List<Order> getOrderList(int page, Integer userId) throws TransactionException, SQLException {
        int pagesCount = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT));
        int rowsPerPage = Integer.valueOf(ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT));
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();

        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<Order> read;
        try {
            TransactionManager.beginTransaction();
            read = orderDAO.read(start, limit, userId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    /**
     * get new order and set current userId to employeeId
     */
    public Order takeNewOrder(int employeeId, OrderStatus status) throws SQLException, TransactionException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        Order order = null;
        try {
            TransactionManager.beginTransaction();
            order = orderDAO.take(status);
            switch (status) {
                case NEW:
                    order.setStatus(OrderStatus.VERIFICATION);
                    order.setManagerId(employeeId);
                    break;
                default:
                    break;
            }
            order.setEmployeeId(employeeId);
            order.setChangeDateDate(new Date(System.currentTimeMillis()));
            orderDAO.update(order);
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
        return order;
    }

    /**
     * Update exist Order or create new
     */
    public void updateOrder(Order oderd) throws TransactionException, SQLException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        try {
            TransactionManager.beginTransaction();
            oderd.setChangeDateDate(new Date(System.currentTimeMillis()));
            if(oderd.getStatus()==OrderStatus.COMPLETE && oderd.getManagerId()!=0)
                oderd.setEmployeeId(oderd.getManagerId());
            if(oderd.getStatus()==OrderStatus.CLOSE)
                oderd.setEmployeeId(oderd.getUserId());
            if (oderd.getId() != null && oderd.getId() != 0) {
                orderDAO.update(oderd);
            } else {
                orderDAO.create(oderd);
            }
            TransactionManager.endTransaction();
        } catch (SQLException e){
            TransactionManager.rollBack();
        }
    }

    public Map<OrderStatus, Map<OrderStats, Integer>> getStats(Integer userId) throws SQLException {
        return DAOFactory.getFactory().getOrderDao().getTotal(userId);
    }
}
