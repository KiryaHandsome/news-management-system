package ru.clevertec.news.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TestConstants {


    String AUTHOR = "author";
    String COMMENT_TEXT = "comment text";
    Integer COMMENT_ID = 77;
    Integer NEWS_ID = 20;
    String NEWS_TEXT = "news text";
    String NEWS_TITLE = "NEWS TITLE";
    LocalDateTime TIME_NOW = LocalDateTime.now();
    Pageable PAGEABLE = PageRequest.of(0, 20);

    String API_PREFIX = "/api/v1";

    // URLs for CommentController
    String GET_COMMENTS_URL = API_PREFIX + "/comments";
    String GET_COMMENTS_BY_NEWS_ID_TEMPLATE_URL = API_PREFIX + "/news/%d/comments";
    String GET_COMMENT_BY_ID_URL = API_PREFIX + "/comments/";
    String CREATE_COMMENT_TEMPLATE_URL = API_PREFIX + "/news/%d/comments";
    String UPDATE_COMMENT_URL = API_PREFIX + "/comments/";
    String DELETE_COMMENT_URL = API_PREFIX + "/comments/";

    // URLs for NewsController
    String GET_NEWS_URL = API_PREFIX + "/news";
    String CREATE_NEWS_URL = API_PREFIX + "/news";
    String GET_NEWS_BY_ID_URL = API_PREFIX + "/news/";
    String UPDATE_NEWS_URL = API_PREFIX + "/news/";
    String DELETE_NEWS_URL = API_PREFIX + "/news/";

}
