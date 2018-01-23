package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.model.entity.User;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class OrdersHistoryServiceTest {

    OrdersHistoryService service;

    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        service = OrdersHistoryService.getService();
    }

    @Test
    public void getOrderHistoryList() throws DaoException, TransactionException, DatabaseException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList();
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryList1() throws DaoException, TransactionException, DatabaseException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList(1, 1, 1);
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryList2() throws DaoException, TransactionException, DatabaseException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList(1, 1, 1, 1);
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryTitles() throws DaoException, TransactionException, DatabaseException {
        service.getOrderHistoryList(1, 1, 1, 1);
        List<String> orderHistoryTitles = service.getOrderHistoryTitles();
        assertNotNull(orderHistoryTitles);
    }

    @Test
    public void getOrderHistoryById() throws DaoException, TransactionException {
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        assertNotNull(orderHistoryById);
    }

    @Test
    public void updateOrderHistoty() throws DaoException, TransactionException, ParseException, DatabaseException {
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        String action = orderHistoryById.getAction();
        orderHistoryById.setAction(Action.GUESTBOOK_COMMENT.name());
        orderHistoryById.setUserId(1);
        service.updateOrderHistory(orderHistoryById);
        orderHistoryById = service.getOrderHistoryById(1);
        String updated = orderHistoryById.getAction();
        assertNotEquals(action, updated);
        orderHistoryById.setNewValue(OrderStatus.NEW.name());
        orderHistoryById.setAction(Action.CHANGE_STATUS.name());
        service.updateOrderHistory(orderHistoryById);
        orderHistoryById.setNewValue(String.valueOf(100));
        orderHistoryById.setAction(Action.CHANGE_PRICE.name());
        service.updateOrderHistory(orderHistoryById);
        orderHistoryById.setNewValue("1");
        orderHistoryById.setAction(Action.CHANGE_EMPLOYEE.name());
        service.updateOrderHistory(orderHistoryById);
        orderHistoryById.setNewValue("2018-01-01");
        orderHistoryById.setAction(Action.CHANGE_DATE.name());
        service.updateOrderHistory(orderHistoryById);
    }

    @Test
    public void deleteOrder() throws DaoException, TransactionException, ParseException, DatabaseException {
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.read(2);
        System.out.println(user);
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        orderHistoryById.setId(0);
        service.updateOrderHistory(orderHistoryById);
        service.deleteOrder(2);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count != 0);
        count = service.count(1);
        assertTrue(count != 0);
    }
}