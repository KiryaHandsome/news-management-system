package ru.clevertec.news.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO implements Serializable {

    private Integer id;
    private String author;
    private String title;
    private LocalDateTime createdAt;
}
