package ua.glushko.commands.impl.orders.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStateComplete implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.PAYMENT);
        order.setMemo(newMemo(order,message,userName));
    }
}
