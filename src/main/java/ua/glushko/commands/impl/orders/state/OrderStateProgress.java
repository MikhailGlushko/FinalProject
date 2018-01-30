package ua.glushko.commands.impl.orders.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStateProgress implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.COMPLETE);
        order.setEmployeeId(order.getManagerId());
        order.setMemo(newMemo(order,message,userName));
    }
}
