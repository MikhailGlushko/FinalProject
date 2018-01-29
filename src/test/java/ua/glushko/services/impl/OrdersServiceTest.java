package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class OrdersServiceTest {

    private OrdersService service;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        service = OrdersService.getService();
        assertNotNull(service);
    }

    @Test
    public void getOrderList() throws DatabaseException {
        List<Order> orderList = service.getOrderList();
        assertNotNull(orderList);
    }

    @Test
    public void getOrderList1() throws DatabaseException {
        List<Order> orderList = service.getOrderList(1, 1, 1);
        assertNotNull(orderList);
    }

    @Test
    public void getOrderTitles() throws DatabaseException {
        service.getOrderList(1, 1, 1);
        List<String> orderTitles = service.getOrderTitles();
        assertNotNull(orderTitles);
    }

    @Test
    public void getOrderById() throws DaoException {
        Order orderById = service.getOrderById(1);
        assertNotNull(orderById);
        orderById = service.getOrderById(100);
        assertNull(orderById);
    }

    @Test
    public void updateOrder() throws TransactionException, DatabaseException {
        Order orderById = service.getOrderById(1);
        OrderStatus status = orderById.getStatus();
        orderById.setStatus(OrderStatus.REJECT);
        service.updateOrder(orderById);
        orderById = service.getOrderById(1);
        OrderStatus read = orderById.getStatus();
        assertNotEquals(read,status);
    }

    @Test
    public void deleteOrder() throws TransactionException, DatabaseException {
        service.deleteOrder(2);
        Order order = service.getOrderById(2);
        assertNull(order);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
        assertTrue(count==0);
    }

    @Test
    public void getOrderList2() throws TransactionException, DatabaseException {
        List<Order> orderList = service.getOrderList(1, 1, 1, 1);
        assertNotNull(orderList);
    }
}