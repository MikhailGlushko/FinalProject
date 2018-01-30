package ua.glushko.commands.impl.orders.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

public class OrderStatePayment implements OrderState {
    @Override
    public void nextStage(Order order, String message, String userName, Integer userId) {
        order.setStatus(OrderStatus.CLOSE);
        order.setEmployeeId(order.getUserId());
        order.setMemo(newMemo(order,message,userName));
    }

    @Override
    public void reject(Order order, String message, String userName) {
        throw new UnsupportedOperationException();
    }
}
