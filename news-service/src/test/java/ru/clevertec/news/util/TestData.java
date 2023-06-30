package ru.clevertec.news.util;

import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.dto.news.NewsResponse;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

import java.time.LocalDateTime;

public class TestData {

    public static Comment getComment() {
        return Comment.builder()
                .id(TestConstants.COMMENT_ID)
                .author(TestConstants.AUTHOR)
                .news(getNews())
                .text(TestConstants.COMMENT_TEXT)
                .createdAt(TestConstants.TIME_NOW)
                .editedAt(TestConstants.TIME_NOW)
                .build();
    }

    public static News getNews() {
        return News.builder()
                .id(TestConstants.NEWS_ID)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.NEWS_TITLE)
                .text(TestConstants.NEWS_TEXT)
                .createdAt(TestConstants.TIME_NOW)
                .editedAt(TestConstants.TIME_NOW)
                .build();
    }

    public static CommentDTO getCommentDTO() {
        return new CommentDTO(
                TestConstants.COMMENT_ID,
                TestConstants.COMMENT_TEXT,
                TestConstants.TIME_NOW,
                TestConstants.TIME_NOW
        );
    }

    public static CommentResponse getCommentResponse() {
        return new CommentResponse(
                TestConstants.COMMENT_ID,
                getNewsDTO(),
                TestConstants.COMMENT_TEXT,
                TestConstants.TIME_NOW,
                TestConstants.TIME_NOW
        );
    }

    public static NewsDTO getNewsDTO() {
        return new NewsDTO(
                TestConstants.NEWS_ID,
                TestConstants.NEWS_TITLE,
                TestConstants.TIME_NOW
        );
    }

    public static CommentRequest getCommentRequest() {
        return new CommentRequest(TestConstants.COMMENT_TEXT);
    }

    public static NewsResponse getNewsResponse() {
        return new NewsResponse(
                TestConstants.NEWS_ID,
                TestConstants.NEWS_TITLE,
                TestConstants.NEWS_TEXT,
                TestConstants.TIME_NOW,
                TestConstants.TIME_NOW
        );
    }

    public static NewsRequest getNewsRequest() {
        return new NewsRequest(
                TestConstants.NEWS_TITLE,
                TestConstants.NEWS_TEXT
        );
    }
}
