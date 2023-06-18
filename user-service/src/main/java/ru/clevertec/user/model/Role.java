package ru.clevertec.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum Role {
    /**
     * User(means unauthorized) has permission to read comments and news
     */
    ROLE_USER(Set.of(
            Permission.NEWS_READ,
            Permission.COMMENT_READ
    )),
    /**
     * Admin has permission to CRUD-operations for all entities
     */
    ROLE_ADMIN(Set.of(
            Permission.NEWS_CREATE,
            Permission.NEWS_READ,
            Permission.NEWS_UPDATE,
            Permission.NEWS_DELETE,
            Permission.COMMENT_CREATE,
            Permission.COMMENT_READ,
            Permission.COMMENT_UPDATE,
            Permission.COMMENT_DELETE
    )),
    /**
     * Journalist has permission for CRUD-operations with his news and comments
     */
    ROLE_JOURNALIST(Set.of(
            Permission.NEWS_CREATE,
            Permission.NEWS_READ,
            Permission.NEWS_UPDATE,
            Permission.NEWS_DELETE,
            Permission.COMMENT_CREATE,
            Permission.COMMENT_READ,
            Permission.COMMENT_UPDATE,
            Permission.COMMENT_DELETE
    )),
    /**
     * Subscriber has permission for CRUD-operations with his comments
     */
    ROLE_SUBSCRIBER(Set.of(
            Permission.NEWS_READ,
            Permission.COMMENT_CREATE,
            Permission.COMMENT_READ,
            Permission.COMMENT_UPDATE,
            Permission.COMMENT_DELETE
    ));

    @Getter
    private final Set<Permission> permissions;
}
