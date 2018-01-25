package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.*;
import ua.glushko.model.entity.*;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdersHistoryService extends Service {

    private OrdersHistoryService() {
    }

    public static OrdersHistoryService getService() {
        return new OrdersHistoryService();
    }

    /** List of Order History */
    public List<OrderHistory> getOrderHistoryList() throws DatabaseException {
        return DAOFactory.getFactory().getOrderHistoryDAO().read();
    }

    /** List of Order History with limit */
    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getOrderHistoryDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of Order History for orderId with limit */
    public List<OrderHistory> getOrderHistoryList(int page, int pagesCount, int rowsPerPage, Integer orderId) throws DatabaseException {
        return DAOFactory.getFactory().getOrderHistoryDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage,orderId);
    }

    /** List of field names */
    public List<String> getOrderHistoryTitles() {
        return DAOFactory.getFactory().getOrderHistoryDAO().getTableHead();
    }

    /** Get Order History by id */
    public OrderHistory getOrderHistoryById(int id) throws DaoException {
        return getById(DAOFactory.getFactory().getOrderHistoryDAO(), id);
    }

    /** Update exist Order History or create new */
    public void updateOrderHistory(OrderHistory orderHistory) throws TransactionException, ParseException, DatabaseException {
        try {
            OrderDAO orderDAO = OrderDAO.getInstance();
            OrderHistoryDAO orderHistoryDAO = OrderHistoryDAO.getInstance();
            OrderQueDAO orderQueDAO = OrderQueDAO.getInstance();
            UserDAO userDAO = UserDAO.getInstance();

            TransactionManager.beginTransaction();

            Order order = orderDAO.read(orderHistory.getOrderId());
            GuestBookDAO guestBookDAO = GuestBookDAO.getInstance();
            orderHistory.setActionDate(new Date(System.currentTimeMillis()));
            OrderQue orderQue = new OrderQue();
            orderQue.setOrderId(orderHistory.getOrderId());
            orderQue.setCreate(new Date(System.currentTimeMillis()));
            orderQue.setMessage(orderHistory.getDescription());
            switch (Action.valueOf(orderHistory.getAction())) {
                case ADD_COMMENT:
                    orderHistory.setOldValue(order.getMemo());
                    order.setMemo(orderHistory.getDescription());
                    orderQue.setRole(UserRole.MANAGER);
                    if(order.getEmployeeId()!=0) {
                        User user = userDAO.read(order.getEmployeeId());
                        orderQue.setEmployeeId(user.getId());
                        orderQue.setRole(user.getRole());
                    }
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
                    orderQue.setRole(UserRole.MANAGER);
                    if(order.getEmployeeId()!=0) {
                        user = userDAO.read(order.getEmployeeId());
                        orderQue.setEmployeeId(user.getId());
                        orderQue.setRole(user.getRole());
                    }
                    break;
                case CHANGE_EMPLOYEE:
                    orderHistory.setOldValue(String.valueOf(order.getEmployeeId()));
                    order.setEmployeeId(Integer.valueOf(orderHistory.getNewValue()));
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setStatus(OrderStatus.PROGRESS);
                    orderQue.setRole(UserRole.MANAGER);
                    if(order.getEmployeeId()!=0) {
                        user = userDAO.read(order.getEmployeeId());
                        orderQue.setEmployeeId(user.getId());
                        orderQue.setRole(user.getRole());
                    }
                    break;
                case CHANGE_PRICE:
                    orderHistory.setOldValue(String.valueOf(order.getPrice()));
                    order.setPrice(Double.valueOf(orderHistory.getNewValue()));
                    if(order.getEmployeeId()!=0) {
                        user = userDAO.read(order.getEmployeeId());
                        orderQue.setEmployeeId(user.getId());
                        orderQue.setRole(user.getRole());
                    }
                    break;
                case CHANGE_STATUS:
                    orderHistory.setOldValue(order.getStatus().name());
                    order.setStatus(OrderStatus.valueOf(orderHistory.getNewValue()));
                    if (order.getStatus() == OrderStatus.CLOSE || order.getStatus() == OrderStatus.REJECT)
                        order.setEmployeeId(order.getUserId());
                    if (order.getStatus() == OrderStatus.NEW)
                        order.setEmployeeId(orderHistory.getUserId());
                    if(order.getEmployeeId()!=0) {
                        user = userDAO.read(order.getEmployeeId());
                        orderQue.setEmployeeId(user.getId());
                        orderQue.setRole(user.getRole());
                    }
                    break;
            }
            if (order.isChanged()) {
                orderDAO.update(order);
                if (orderHistory.getId() != null && orderHistory.getId() != 0)
                    orderHistoryDAO.update(orderHistory);
                else
                    orderHistoryDAO.create(orderHistory);
            }
            orderQueDAO.create(orderQue);
            TransactionManager.endTransaction();
        } catch (NumberFormatException e){
            LOGGER.error(e);
        } finally {
            TransactionManager.rollBack();
        }
    }

    /** Delete exist Order History */
    public void deleteOrder(Integer serviceId) throws TransactionException, DatabaseException {
        delete(DAOFactory.getFactory().getOrderHistoryDAO(), serviceId);
    }

    /** Total of Order History*/
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getOrderHistoryDAO());
    }

    /** Total of Order History by orderId*/
    public int count(int orderId) throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getOrderHistoryDAO(), orderId);
    }
}
