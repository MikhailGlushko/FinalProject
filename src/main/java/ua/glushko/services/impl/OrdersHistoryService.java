package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.OrderDAO;
import ua.glushko.model.dao.impl.OrderHistoryDAO;
import ua.glushko.model.entity.Action;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderHistory;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

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

    public List<OrderHistory> getOrderHistoryList() throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO());
    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), page, pagesCount, rowsPerPage);
    }

    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage, int userId) throws PersistException, TransactionException {
        return (List<OrderHistory>) getList(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), page, pagesCount, rowsPerPage, userId);
    }

    public List<String> getOrderHistoryTitles() {
        return MySQLDAOFactory.getFactory().getOrderHistoryDAO().getTableHead();
    }

    public OrderHistory getOrderHistoryById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), id);
    }

    public void updateOrderHistoty(OrderHistory item) throws PersistException, TransactionException, ParseException {
        //update(MySQLDAOFactory.getFactory().getOrderHistoryDAO(),item);
        try {
            TransactionManager.beginTransaction();
            OrderDAO orderDAO = OrderDAO.getInstance();
            OrderHistoryDAO orderHistoryDAO = OrderHistoryDAO.getInstance();
            Order order = orderDAO.read(item.getOrderId());
            item.setActionDate(new Date(System.currentTimeMillis()));
            switch (Action.valueOf(item.getAction())) {
                case ADD_COMMENT:
                    item.setOldValue(order.getMemo());
                    order.setMemo(item.getNewValue());
                    break;
                case CHANGE_DATE:
                    Date expectedDate = order.getExpectedDate();
                    if (expectedDate != null)
                        item.setOldValue(order.getExpectedDate().toString());
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date date = format.parse(item.getNewValue());
                    order.setExpectedDate(date);
                    break;
                case CHANGE_EMPLOYEE:
                    item.setOldValue(String.valueOf(order.getEmployeeId()));
                    order.setEmployeeId(Integer.valueOf(item.getNewValue()));
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setStatus(OrderStatus.INWORK);
                    break;
                case CHANGE_PRICE:
                    item.setOldValue(String.valueOf(order.getPrice()));
                    order.setPrice(Double.valueOf(item.getNewValue()));
                    break;
                case CHANGE_STATUS:
                    item.setOldValue(order.getStatus().name());
                    order.setStatus(OrderStatus.valueOf(item.getNewValue()));
                    if (order.getStatus() == OrderStatus.CLOSE || order.getStatus() == OrderStatus.REJECT)
                        order.setEmployeeId(order.getUserId());
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setEmployeeId(item.getUserId());
                    break;
            }
            if (order.isChanched()) {
                orderDAO.update(order);
                if (item.getId() != null && item.getId() != 0)
                    orderHistoryDAO.update(item);
                else
                    orderHistoryDAO.create(item);
            }
            TransactionManager.endTransaction();
        } catch (NumberFormatException e){
            LOGGER.error(e);
        } finally {
            TransactionManager.rollBack();
        }
    }

    public void deleteOrder(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), serviceId);
    }

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderHistoryDAO());
    }

    public int count(int id) throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getOrderHistoryDAO(), id);
    }
}
