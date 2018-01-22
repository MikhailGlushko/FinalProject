package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GuestBookDAO;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.dao.impl.OrderHistoryDAO;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.*;
import ua.glushko.model.exception.DaoException;
import ua.glushko.model.exception.DatabaseException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdersHistoryService extends AbstractService {

    private OrdersHistoryService() {
    }

    public static OrdersHistoryService getService() {
        return new OrdersHistoryService();
    }

    public List<OrderHistory> getOrderHistoryList() throws TransactionException, DatabaseException {
        OrderHistoryDAO orderHistoryDAO = MySQLDAOFactory.getFactory().getOrderHistoryDAO();
        List<OrderHistory> read;
        try{
            TransactionManager.beginTransaction();
            read = orderHistoryDAO.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;

    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
        OrderHistoryDAO orderHistoryDAO = MySQLDAOFactory.getFactory().getOrderHistoryDAO();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<OrderHistory> read;
        try {
            TransactionManager.beginTransaction();
            read = orderHistoryDAO.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;

    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage, Integer id) throws TransactionException, DatabaseException {

        OrderHistoryDAO orderHistoryDAO = MySQLDAOFactory.getFactory().getOrderHistoryDAO();

        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<OrderHistory> read;
        try {
            TransactionManager.beginTransaction();
            read = orderHistoryDAO.read(start, limit,id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getOrderHistoryTitles() {
        return MySQLDAOFactory.getFactory().getOrderHistoryDAO().getTableHead();
    }

    public OrderHistory getOrderHistoryById(int id) throws DaoException {
        return getById(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), id);
    }

    public void updateOrderHistory(OrderHistory orderHistory) throws TransactionException, ParseException, DatabaseException {
        try {
            TransactionManager.beginTransaction();
            OrderDAO orderDAO = OrderDAO.getInstance();
            OrderHistoryDAO orderHistoryDAO = OrderHistoryDAO.getInstance();
            Order order = orderDAO.read(orderHistory.getOrderId());
            GuestBookDAO guestBookDAO = GuestBookDAO.getInstance();
            orderHistory.setActionDate(new Date(System.currentTimeMillis()));
            UserDAO userDAO = UserDAO.getInstance();
            switch (Action.valueOf(orderHistory.getAction())) {
                case ADD_COMMENT:
                    orderHistory.setOldValue(order.getMemo());
                    order.setMemo(orderHistory.getDescription());
                    break;
                case GUESTBOOK_COMMENT:
                    GuestBook guestBook = new GuestBook();
                    guestBook.setActionDate(new Date(System.currentTimeMillis()));
                    guestBook.setDescription(Action.GUESTBOOK_COMMENT.name());
                    guestBook.setMemo(orderHistory.getDescription());
                    guestBook.setOrderId(orderHistory.getOrderId());
                    int userId = orderHistory.getUserId();
                    User user = userDAO.read(userId);
                    String name = user.getName();
                    guestBook.setUserName(name);
                    guestBookDAO.create(guestBook);
                    break;
                case CHANGE_DATE:
                    Date expectedDate = order.getExpectedDate();
                    if (expectedDate != null)
                        orderHistory.setOldValue(order.getExpectedDate().toString());
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date date = format.parse(orderHistory.getNewValue());
                    order.setExpectedDate(date);
                    break;
                case CHANGE_EMPLOYEE:
                    orderHistory.setOldValue(String.valueOf(order.getEmployeeId()));
                    order.setEmployeeId(Integer.valueOf(orderHistory.getNewValue()));
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setStatus(OrderStatus.INWORK);
                    break;
                case CHANGE_PRICE:
                    orderHistory.setOldValue(String.valueOf(order.getPrice()));
                    order.setPrice(Double.valueOf(orderHistory.getNewValue()));
                    break;
                case CHANGE_STATUS:
                    orderHistory.setOldValue(order.getStatus().name());
                    order.setStatus(OrderStatus.valueOf(orderHistory.getNewValue()));
                    if (order.getStatus() == OrderStatus.CLOSE || order.getStatus() == OrderStatus.REJECT)
                        order.setEmployeeId(order.getUserId());
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setEmployeeId(orderHistory.getUserId());
                    break;
            }
            if (order.isChanged()) {
                orderDAO.update(order);
                if (orderHistory.getId() != null && orderHistory.getId() != 0)
                    orderHistoryDAO.update(orderHistory);
                else
                    orderHistoryDAO.create(orderHistory);
            }
            TransactionManager.endTransaction();
        } catch (NumberFormatException e){
            LOGGER.error(e);
        } finally {
            TransactionManager.rollBack();
        }
    }

    public void deleteOrder(Integer serviceId) throws TransactionException, DatabaseException {
        delete(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderHistoryDAO());
    }

    public int count(int id) throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), id);
    }
}
