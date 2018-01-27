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

    OrdersService service;
    @Before
    @Test
    public void getService() {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
        service = OrdersService.getService();
    }

    @Test
    public void getOrderList() throws DaoException, TransactionException, DatabaseException {
        List<Order> orderList = service.getOrderList();
        assertNotNull(orderList);
    }

    @Test
    public void getOrderList1() throws DaoException, TransactionException, DatabaseException {
        List<Order> orderList = service.getOrderList(1, 1, 1);
        assertNotNull(orderList);
    }

    @Test
    public void getOrderTitles() throws DaoException, TransactionException, DatabaseException {
        service.getOrderList(1, 1, 1);
        List<String> orderTitles = service.getOrderTitles();
        assertNotNull(orderTitles);
    }

    @Test
    public void getOrderById() throws DaoException, TransactionException {
        Order orderById = service.getOrderById(1);
        assertNotNull(orderById);
        orderById = service.getOrderById(100);
        assertNull(orderById);
    }

    @Test
    public void updateOrder() throws DaoException, TransactionException, DatabaseException {
        Order orderById = service.getOrderById(1);
        OrderStatus status = orderById.getStatus();
        orderById.setStatus(OrderStatus.REJECT);
        service.updateOrder(orderById);
        orderById = service.getOrderById(1);
        OrderStatus read = orderById.getStatus();
        assertNotEquals(read,status);
    }

    @Test
    public void deleteOrder() throws DaoException, TransactionException, DatabaseException {
        service.deleteOrder(2);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
        count = service.count(1);
    }

    @Test
    public void getOrderList2() throws DaoException, TransactionException, DatabaseException {
        List<Order> orderList = service.getOrderList(1, 1, 1, 1);
    }

    @Test
    public void AssignTo() throws DatabaseException, TransactionException {
        //Integer before = service.countNewWithoutEmployee(OrderStatus.NEW);
        //assertNotNull(before);
        //Order order = service.takeNewOrder(2,OrderStatus.NEW);
        //assertNotNull(order);
        //Integer after = service.countNewWithoutEmployee(OrderStatus.NEW);
        //assertNotEquals(before,after);
    }
}