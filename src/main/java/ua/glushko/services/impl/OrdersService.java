package ua.glushko.services.impl;

import ua.glushko.model.dao.AbstractDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.dao.impl.OrderQueDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.*;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrdersService extends AbstractService {

    private OrdersService() {
    }

    public static OrdersService getService() {
        return new OrdersService();
    }

    public List<Order> getOrderList() throws TransactionException, DatabaseException {
        OrderDAO orderDao = MySQLDAOFactory.getFactory().getOrderDao();
        List<Order> read;
        try{
            TransactionManager.beginTransaction();
            read = orderDao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
        OrderDAO orderDao = MySQLDAOFactory.getFactory().getOrderDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<Order> read;
        try {
            TransactionManager.beginTransaction();
            read = orderDao.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getOrderTitles() {
        return  MySQLDAOFactory.getFactory().getOrderDao().getTableHead();
    }

    public Order getOrderById(int id) throws DaoException {
        return getById(MySQLDAOFactory.getFactory().getOrderDao(),id);
    }

    //public void updateOrder(Order item) throws TransactionException, DatabaseException {
    //    update(MySQLDAOFactory.getFactory().getOrderDao(),item);
    //}

    public void deleteOrder(Integer serviceId) throws TransactionException, DatabaseException {
        delete(MySQLDAOFactory.getFactory().getOrderDao(),serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderDao());
    }

    public int count(int id) throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderDao(),id);
    }

    public List<Order> getOrderList(int page, int pagesCount, int rowsPerPage, Integer id) throws TransactionException, DatabaseException {
        OrderDAO orderDAO = MySQLDAOFactory.getFactory().getOrderDao();

        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<Order> read;
        try {
            TransactionManager.beginTransaction();
            read = orderDAO.read(start, limit,id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public Order takeNewOrder(int employeeId) throws DatabaseException, TransactionException {
        OrderDAO orderDAO = MySQLDAOFactory.getFactory().getOrderDao();
        Order order;
        try{
            TransactionManager.beginTransaction();
            order = orderDAO.takeNew();
            order.setEmployeeId(employeeId);
            if(order.getStatus()== OrderStatus.NEW)
                order.setStatus(OrderStatus.INWORK);
            orderDAO.update(order);
            order = orderDAO.read(order.getId());
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return order;
    }

    public Integer countNew() throws DaoException {
        OrderDAO orderDAO = MySQLDAOFactory.getFactory().getOrderDao();
        return orderDAO.countNew();
    }

    public void updateOrder(Order item) throws TransactionException, DatabaseException {
        OrderQueDAO orderQueDAO = MySQLDAOFactory.getFactory().getOrderQueDao();
        UserDAO userDAO = MySQLDAOFactory.getFactory().getUserDao();
        OrderDAO orderDAO = MySQLDAOFactory.getFactory().getOrderDao();
        try{
            TransactionManager.beginTransaction();
            if(item.getId()!=null && item.getId()!=0) {
                orderDAO.update(item);
                OrderQue orderQue = new OrderQue();
                orderQue.setRole(UserRole.MANAGER);
                if(item.getEmployeeId()!=0){
                    User user = userDAO.read(item.getEmployeeId());
                    orderQue.setEmployeeId(user.getId());
                    orderQue.setRole(user.getRole());
                    orderQue.setCreate(new Date(System.currentTimeMillis()));
                    orderQue.setMessage("UPDATE_ORDER");
                    orderQueDAO.create(orderQue);
                }
            }
            else {
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
}
