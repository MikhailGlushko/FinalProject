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
     * get new order and set current userId to employeeId
     */
    public void takeNewOrder(int employeeId, OrderStatus status) throws DatabaseException, TransactionException {
        OrderDAO orderDAO = DAOFactory.getFactory().getOrderDao();
        OrderQueDAO orderQueDAO = DAOFactory.getFactory().getOrderQueDao();
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        try {
            TransactionManager.beginTransaction();
            Order order = orderDAO.take(status);
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
    }

    /**
     * Update exist Order or create new
     */
    public void updateOrder(Order oderd) throws TransactionException, DatabaseException {
        OrderQueDAO orderQueDAO = DAOFactory.getFactory().getOrderQueDao();
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
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
                OrderQue orderQue = new OrderQue();
                orderQue.setRole(UserRole.MANAGER);
                if (oderd.getEmployeeId() != 0) {
                    User user = userDAO.read(oderd.getEmployeeId());
                    orderQue.setEmployeeId(user.getId());
                    orderQue.setRole(user.getRole());
                    orderQue.setCreate(new Date(System.currentTimeMillis()));
                    orderQue.setMessage("UPDATE_ORDER");
                    orderQueDAO.create(orderQue);
                }
            } else {
                orderDAO.create(oderd);
                OrderQue orderQue = new OrderQue();
                orderQue.setOrderId(oderd.getId());
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

    public Map<OrderStatus, Map<OrderStats, Integer>> getStats(Integer userId) throws DaoException {
        return DAOFactory.getFactory().getOrderDao().getTotal(userId);
    }
}
