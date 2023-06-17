package ru.clevertec.news.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TestConstants {

    String COMMENT_TEXT = "comment text";
    Integer COMMENT_ID = 77;
    Integer NEWS_ID = 20;
    String NEWS_TEXT = "news text";
    String NEWS_TITLE = "NEWS TITLE";
    LocalDateTime TIME_NOW = LocalDateTime.now();
    Pageable PAGEABLE = PageRequest.of(0, 20);


    // URLs for CommentController
    String GET_COMMENTS_URL = "/comments";
    String GET_COMMENTS_BY_NEWS_ID_TEMPLATE_URL = "/news/%d/comments";
    String GET_COMMENT_BY_ID_URL = "/comments/";
    String CREATE_COMMENT_TEMPLATE_URL = "/news/%d/comments";
    String UPDATE_COMMENT_URL = "/comments/";
    String DELETE_COMMENT_URL = "/comments/";

    // URLs for NewsController
    String GET_NEWS_URL = "/news";
    String CREATE_NEWS_URL = "/news";
    String GET_NEWS_BY_ID_URL = "/news/";
    String UPDATE_NEWS_URL = "/news/";
    String DELETE_NEWS_URL = "/news/";
}
