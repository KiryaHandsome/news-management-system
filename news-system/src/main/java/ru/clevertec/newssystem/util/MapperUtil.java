package ru.clevertec.newssystem.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.newssystem.dto.comment.CommentRequest;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.model.Comment;
import ru.clevertec.newssystem.model.News;

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
