package ru.clevertec.news.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

import java.time.LocalDateTime;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBuilder {

    private Integer id = TestConstants.COMMENT_ID;
    private News news = new NewsBuilder().build();
    private String text = TestConstants.COMMENT_TEXT;
    private LocalDateTime createdAt = TestConstants.TIME_NOW;
    private LocalDateTime editedAt = TestConstants.TIME_NOW;

    public Comment build() {
        return Comment.builder()
                .id(id)
                .news(news)
                .text(text)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .build();
    }
}
