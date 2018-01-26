package ua.glushko.model.entity;

/** Order status */
public enum OrderStatus {
    NEW,            //new order
    VERIFICATION,   // verification by manager
    ESTIMATE,       // estimate by master
    CONFIRMATION,   // confirmation by customer
    PROGRESS,       // working by master
    COMPLETE,       // complete and need attention of manager
    PAYMENT,        // information to customer for payment
    CLOSE,          // close
    REJECT,         // reject
    SUSPEND         // suspend by master
}
