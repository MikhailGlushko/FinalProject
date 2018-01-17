package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class OrdersHistoryServiceTest {

    OrdersHistoryService service;

    @Before
    @Test
    public void getService() {
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
        service = OrdersHistoryService.getService();
    }

    @Test
    public void getOrderHistoryList() throws PersistException, TransactionException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList();
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryList1() throws PersistException, TransactionException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList(1, 1, 1);
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryList2() throws PersistException, TransactionException {
        List<OrderHistory> orderHistoryList = service.getOrderHistoryList(1, 1, 1, 1);
        assertNotNull(orderHistoryList);
    }

    @Test
    public void getOrderHistoryTitles() throws PersistException, TransactionException {
        service.getOrderHistoryList(1, 1, 1, 1);
        List<String> orderHistoryTitles = service.getOrderHistoryTitles();
        assertNotNull(orderHistoryTitles);
    }

    @Test
    public void getOrderHistoryById() throws PersistException, TransactionException {
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        assertNotNull(orderHistoryById);
    }

    @Test
    public void updateOrderHistoty() throws PersistException, TransactionException, ParseException {
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        String action = orderHistoryById.getAction();
        orderHistoryById.setAction(Action.GUESTBOOK_COMMENT.name());
        orderHistoryById.setUserId(1);
        service.updateOrderHistoty(orderHistoryById);
        orderHistoryById = service.getOrderHistoryById(1);
        String updated = orderHistoryById.getAction();
        assertNotEquals(action, updated);
        orderHistoryById.setNewValue(OrderStatus.NEW.name());
        orderHistoryById.setAction(Action.CHANGE_STATUS.name());
        service.updateOrderHistoty(orderHistoryById);
        orderHistoryById.setNewValue(String.valueOf(100));
        orderHistoryById.setAction(Action.CHANGE_PRICE.name());
        service.updateOrderHistoty(orderHistoryById);
        orderHistoryById.setNewValue("1");
        orderHistoryById.setAction(Action.CHANGE_EMPLOYEE.name());
        service.updateOrderHistoty(orderHistoryById);
        orderHistoryById.setNewValue("2018-01-01");
        orderHistoryById.setAction(Action.CHANGE_DATE.name());
        service.updateOrderHistoty(orderHistoryById);
    }

    @Test
    public void deleteOrder() throws PersistException, TransactionException, ParseException {
        OrderHistory orderHistoryById = service.getOrderHistoryById(1);
        orderHistoryById.setId(0);
        service.updateOrderHistoty(orderHistoryById);
        service.deleteOrder(2);
    }

    @Test
    public void count() throws PersistException, TransactionException {
        int count = service.count();
        assertTrue(count != 0);
        count = service.count(1);
        assertTrue(count != 0);
    }
}