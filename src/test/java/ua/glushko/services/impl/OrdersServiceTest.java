package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.util.List;

import static org.junit.Assert.*;

public class OrdersServiceTest {

    OrdersService service;
    @Before
    @Test
    public void getService() {
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
        service = OrdersService.getService();
    }

    @Test
    public void getOrderList() throws PersistException, TransactionException {
        List<Order> orderList = service.getOrderList();
        assertNotNull(orderList);
    }

    @Test
    public void getOrderList1() throws PersistException, TransactionException {
        List<Order> orderList = service.getOrderList(1, 1, 1);
        assertNotNull(orderList);
    }

    @Test
    public void getOrderTitles() throws PersistException, TransactionException {
        service.getOrderList(1, 1, 1);
        List<String> orderTitles = service.getOrderTitles();
        assertNotNull(orderTitles);
    }

    @Test
    public void getOrderById() throws PersistException, TransactionException {
        Order orderById = service.getOrderById(1);
        assertNotNull(orderById);
        orderById = service.getOrderById(100);
        assertNull(orderById);
    }

    @Test
    public void updateOrder() throws PersistException, TransactionException {
        Order orderById = service.getOrderById(1);
        OrderStatus status = orderById.getStatus();
        orderById.setStatus(OrderStatus.REJECT);
        service.updateOrder(orderById);
        orderById = service.getOrderById(1);
        OrderStatus read = orderById.getStatus();
        assertNotEquals(read,status);
    }

    @Test
    public void deleteOrder() throws PersistException, TransactionException {
        service.deleteOrder(2);
    }

    @Test
    public void count() throws PersistException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
    }

    @Test
    public void getOrderList2() throws PersistException, TransactionException {
        List<Order> orderList = service.getOrderList(1, 1, 1, 1);
    }
}