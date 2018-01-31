package ua.glushko.model.entity.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStateClose implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.NEW);
        order.setMemo(newMemo(order,message,userName));
        order.setManagerId(0);
        order.setEmployeeId(0);
    }

    @Override
    public void reject(Order order, String message, String userName) {
        throw new UnsupportedOperationException();
    }
}
