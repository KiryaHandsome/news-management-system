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

    private Integer id = TestConstants.NEWS_ID;
    private String title = TestConstants.NEWS_TITLE;
    private String text = TestConstants.NEWS_TEXT;
    private LocalDateTime createdAt = TestConstants.TIME_NOW;
    private LocalDateTime editedAt = TestConstants.TIME_NOW;
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
