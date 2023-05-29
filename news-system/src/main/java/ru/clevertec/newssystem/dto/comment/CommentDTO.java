package ru.clevertec.newssystem.dto.comment;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Integer id;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
