package ua.glushko.model.entity.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStateVerification implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.ESTIMATE);
        order.setEmployeeId(userId);
        order.setManagerId(userId);
        order.setMemo(newMemo(order,message,userName));
    }
}
