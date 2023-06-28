package ru.clevertec.news.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

@UtilityClass
public class MapperUtil {

    public static void mapCommentIfNotNull(Comment comment, CommentRequest request) {
        if(request.getText() != null) {
            comment.setText(request.getText());
        }
    }

    public static void mapNewsIfNotNull(News news, NewsRequest request) {
        if(request.getText() != null) {
            news.setText(request.getText());
        }
        if(request.getTitle() != null) {
            news.setTitle(request.getTitle());
        }
    }
}
