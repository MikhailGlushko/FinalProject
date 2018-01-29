package ua.glushko.model.entity;

import org.junit.Test;

public class OrderStatusTest {

    @Test
    public void test(){
        OrderStatus orderStatus = OrderStatus.VERIFICATION;
        System.out.println(orderStatus);
        int ordinal = orderStatus.ordinal();
        System.out.println(ordinal);
        OrderStatus[] values = OrderStatus.values();
        System.out.println(values[ordinal]);
        System.out.println(orderStatus.getClass().getSuperclass());
    }

}