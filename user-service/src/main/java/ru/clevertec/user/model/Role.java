package ru.clevertec.user.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    /**
     * Admin has permission to CRUD-operations for all entities
     */
    ADMIN,
    /**
     * Journalist has permission for CRUD-operations with his news and comments
     */
    JOURNALIST,
    /**
     * Subscriber has permission for CRUD-operations with his comments
     */
    SUBSCRIBER;
}
