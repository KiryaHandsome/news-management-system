package ru.clevertec.news.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.news.dto.news.NewsDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private NewsDTO news;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
