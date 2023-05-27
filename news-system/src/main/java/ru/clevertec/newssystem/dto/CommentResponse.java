package ru.clevertec.newssystem.dto;

import lombok.Data;
import ru.clevertec.newssystem.model.News;

import java.time.LocalDate;

@Data
public class CommentResponse {

    private Integer id;
    private News news;
    private String text;
    private LocalDate createdAt;
    private LocalDate editedAt;
}
