package ru.clevertec.newssystem.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Integer id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
