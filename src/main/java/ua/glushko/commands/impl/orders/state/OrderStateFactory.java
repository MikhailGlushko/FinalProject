package ua.glushko.commands.impl.orders.state;

import ua.glushko.model.entity.OrderStatus;

public class OrderStateFactory {
    public static OrderState getSate(OrderStatus status){
        switch (status){
            case NEW:           return new OrderStateNew();
            case VERIFICATION:  return new OrderStateVerification();
            case ESTIMATE:      return new OrderStateEstimate();
            case CONFIRMATION:  return new OrderStateConfirmation();
            case PROGRESS:      return new OrderStateProgress();
            case SUSPEND:       return new OrderStateSuspend();
            case COMPLETE:      return new OrderStateComplete();
            case PAYMENT:       return new OrderStatePayment();
            case REJECT:        return new OrderStateReject();
            case CLOSE:         return new OrderStateClose();
            default: return null;
        }
    }
}
