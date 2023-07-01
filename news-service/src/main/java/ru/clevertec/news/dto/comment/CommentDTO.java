package ru.clevertec.news.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
    private String author;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
