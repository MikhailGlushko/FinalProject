package ua.glushko.model.entity;

/**
 * User roles
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
