package ua.glushko.model.entity;

/**
 * User statuses
 * @author Mikhail Glushko
 * @version 1.0
 * Access to the system is granted only to users with the status ACTIVE
 */
public enum UserStatus {
    ACTIVE,     // active and can login into system
    CLOSE,      // closed   - no access
    BLOCKED     // blocked  - no access
}