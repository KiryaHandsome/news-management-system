package ru.clevertec.newssystem.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
