package ua.glushko.model.entity;

/**
 * User roles
 * @author Mikhail Glushko
 * @version 1.0
 * ADMIN , MANAGER , MASTER - workers
 * CUSTOMER - customers
 * GUEST - guests
 */
public enum UserRole {
    ADMIN ,     // system admin
    MANAGER ,   // manager
    MASTER,     // master
    CUSTOMER,   // customer
    GUEST       // reserved
}
