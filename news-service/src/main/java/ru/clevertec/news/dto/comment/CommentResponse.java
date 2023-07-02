package ru.clevertec.news.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.news.dto.news.NewsDTO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse implements Serializable {

    private Integer id;
    private String text;
    private String author;
    private NewsDTO news;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
