package ua.glushko.model.entity;

/**
 * Статусы пользователей
 * Доступ к системе предоставляется только пользователям со статусом ACTIVE
 */
public enum UserStatus {
    ACTIVE, CLOSE, BLOCKED
}