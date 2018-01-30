package ua.glushko.model.entity;

/**
 * Statuses for Orders
 * @author Mikhail Glushko
 * @version 1.0
 */
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
