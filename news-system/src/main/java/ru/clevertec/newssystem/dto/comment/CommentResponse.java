package ru.clevertec.newssystem.dto.comment;

import lombok.Data;
import ru.clevertec.newssystem.dto.news.NewsDTO;
import ru.clevertec.newssystem.dto.news.NewsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Integer id;
    private NewsDTO news;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
