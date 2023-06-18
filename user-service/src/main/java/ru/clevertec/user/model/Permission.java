package ru.clevertec.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    NEWS_READ("news:read"),
    NEWS_UPDATE("news:update"),
    NEWS_CREATE("news:create"),
    NEWS_DELETE("news:delete"),
    COMMENT_READ("comment:read"),
    COMMENT_UPDATE("comment:update"),
    COMMENT_CREATE("comment:create"),
    COMMENT_DELETE("comment:delete");

    @Getter
    private final String permission;
}