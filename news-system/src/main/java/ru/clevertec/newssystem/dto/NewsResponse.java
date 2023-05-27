package ru.clevertec.newssystem.dto;

import lombok.Data;
import ru.clevertec.newssystem.model.Comment;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewsResponse {

    private Integer id;
    private String title;
    private String text;
    private LocalDate createdAt;
    private LocalDate editedAt;
    private List<Comment> comments;
}
