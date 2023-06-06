package ru.clevertec.news.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Integer id;
    private String title;
    private LocalDateTime createdAt;
}
