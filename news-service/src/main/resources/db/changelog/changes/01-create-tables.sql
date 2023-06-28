CREATE SCHEMA IF NOT EXISTS news_system;

CREATE TABLE IF NOT EXISTS news_system.news
(
    id         SERIAL PRIMARY KEY,
    title      VARCHAR(100) NOT NULL,
    author     VARCHAR(20)  NOT NULL,
    "text"     TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    edited_at  TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS news_system.comments
(
    id         SERIAL PRIMARY KEY,
    news_id    SERIAL REFERENCES news_system.news (id),
    author     VARCHAR(20) NOT NULL,
    "text"     TEXT        NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    edited_at  TIMESTAMP   NOT NULL
);