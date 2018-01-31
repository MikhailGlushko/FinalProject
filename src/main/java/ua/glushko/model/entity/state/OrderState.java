package ua.glushko.model.entity.state;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

import java.util.Date;
import java.util.Objects;

public interface OrderState {

    void nextStage(Order order, String message, String userName, Integer userId);

    default void reject(Order order, String message, String userName) throws UnsupportedOperationException{
        order.setStatus(OrderStatus.REJECT);
        order.setEmployeeId(order.getUserId());
        order.setMemo(newMemo(order,message,userName));
    }

    default String newMemo (Order order, String message, String userName){
        String oldMemo = order.getMemo();
        if(Objects.isNull(oldMemo))
            oldMemo="";
        return (new StringBuilder(oldMemo).append("\n")
                .append(new Date(System.currentTimeMillis())).append(" ")
                .append(userName).append(" ")
                .append(OrderStatus.REJECT).append(" ")
                .append(order.getStatus())
                .append(message).toString());
    }
}
