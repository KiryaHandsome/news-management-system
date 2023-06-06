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

    private Integer id = 1;
    private News news = new NewsBuilder().build();
    private String text = "TestText";
    private LocalDateTime createdAt = null;
    private LocalDateTime editedAt = null;

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
