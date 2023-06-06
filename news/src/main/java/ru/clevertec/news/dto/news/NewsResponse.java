package ru.clevertec.news.dto.news;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsResponse {

    private Integer id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
