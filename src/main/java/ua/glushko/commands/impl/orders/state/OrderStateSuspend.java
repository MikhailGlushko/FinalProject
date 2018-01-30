package ua.glushko.commands.impl.orders.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStateSuspend implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.PROGRESS);
        order.setMemo(newMemo(order,message,userName));
    }
}
