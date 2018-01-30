package ua.glushko.model.entity;

/**
 * Stats for Orders
 * @version 1.0
 * @author Mikhail Glushko
 */
public enum OrderStats {
    STATUS, // count orders with current status
    NEW,    // count orders latest created by all customers
    TODAY,  // count orders changed today
    OWNER,  // count order created by current user
    EXECUTION,// count orders by current employee
    NO_EMPLOYEE,// count orders without any employee
    CURRENT_USER,// OWNER + EMPLOYEE + additional
    ALL     // count of all orders
}
