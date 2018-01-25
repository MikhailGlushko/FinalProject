package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.dao.impl.OrderQueDAO;
import ua.glushko.model.dao.impl.UserDAO;
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

public class OrdersService extends Service {

    private OrdersService() {
    }

    public static OrdersService getService() {
        return new OrdersService();
    }

    /**
     * List of Orders
     */
    public List<Order> getOrderList() throws DatabaseException {
        return DAOFactory.getFactory().getOrderDao().read();
    }

    /**
     * List of Orders by limit
     */
    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getOrderDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /**
     * List of Orders for status
     */
    public List<Order> getOrderListByStatus(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getOrderDao().readByStatus((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
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
    public Order getOrderById(int id) throws DaoException {
        return getById(DAOFactory.getFactory().getOrderDao(), id);
    }

    /**
     * Delete exist order
     */
    public void deleteOrder(Integer serviceId) throws TransactionException, DatabaseException {
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
    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage, Integer userId) throws TransactionException, DatabaseException {
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
     * List of Orders for status with limit
     */
    public List<Order> getOrderListByStatus(int page, int pagesCount, int rowsPerPage, Integer id) throws TransactionException, DatabaseException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();

        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<Order> read;
        try {
            TransactionManager.beginTransaction();
            read = orderDAO.readByStatus(start, limit, id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    /**
     * get new order and set current userId to employeeId
     */
    public Order takeNewOrder(int employeeId, OrderStatus status) throws DatabaseException, TransactionException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        OrderQueDAO orderQueDAO = DAOFactory.getFactory().getOrderQueDao();
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
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
            orderDAO.update(order);
            //order = orderDAO.read(order.getId());
            User user = userDAO.read(employeeId);
            // create new OrderQue for user
            OrderQue orderQue = new OrderQue();
            orderQue.setMessage("Рассмотреть заказ № " + order.getId());
            orderQue.setOrderId(order.getId());
            orderQue.setCreate(new Date(System.currentTimeMillis()));
            orderQue.setRole(user.getRole());
            orderQue.setEmployeeId(employeeId);
            orderQueDAO.create(orderQue);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return order;
    }

    /**
     * Total of Orders with status==NEW
     */
    public Integer countNewWithoutEmployee(OrderStatus status) throws DaoException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        return orderDAO.countWithoutEmployeeByStatus(status);
    }

    /**
     * Update exist Order or create new
     */
    public void updateOrder(Order item) throws TransactionException, DatabaseException {
        OrderQueDAO orderQueDAO = DAOFactory.getFactory().getOrderQueDao();
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        try {
            TransactionManager.beginTransaction();
            if (item.getId() != null && item.getId() != 0) {
                orderDAO.update(item);
                OrderQue orderQue = new OrderQue();
                orderQue.setRole(UserRole.MANAGER);
                if (item.getEmployeeId() != 0) {
                    User user = userDAO.read(item.getEmployeeId());
                    orderQue.setEmployeeId(user.getId());
                    orderQue.setRole(user.getRole());
                    orderQue.setCreate(new Date(System.currentTimeMillis()));
                    orderQue.setMessage("UPDATE_ORDER");
                    orderQueDAO.create(orderQue);
                }
            } else {
                orderDAO.create(item);
                OrderQue orderQue = new OrderQue();
                orderQue.setOrderId(item.getId());
                orderQue.setRole(UserRole.MANAGER);
                orderQue.setCreate(new Date(System.currentTimeMillis()));
                orderQue.setMessage("NEW_ORDER");
                orderQueDAO.create(orderQue);
            }
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    /**
     * Get stats for count all Orders per status
     */
    public Map<OrderStatus, Integer> getTotalsByStatus() throws SQLException {
        return DAOFactory.getFactory().getOrderDao().getTotalsByStatus();
    }

    /**
     * Get stats for count Orders per status created today
     */
    public Map<OrderStatus, Integer> getNewByStatus() throws SQLException {
        return DAOFactory.getFactory().getOrderDao().getNewByStatus();
    }
}
