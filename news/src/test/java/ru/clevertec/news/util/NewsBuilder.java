package ru.clevertec.news.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsBuilder {

    private Integer id = 1;
    private String title = "TestTitle";
    private String text = "TestText";
    private LocalDateTime createdAt = null;
    private LocalDateTime editedAt = null;
    private List<Comment> comments = new ArrayList<>();

    public News build() {
        return News.builder()
                .id(id)
                .title(title)
                .text(text)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .comments(comments)
                .build();
    }
}
