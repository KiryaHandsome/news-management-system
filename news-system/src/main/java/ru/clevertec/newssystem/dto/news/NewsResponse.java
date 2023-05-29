package ru.clevertec.newssystem.dto.news;

import lombok.Data;
import org.springframework.data.domain.Page;
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.comment.CommentResponse;
import ru.clevertec.newssystem.model.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NewsResponse {

    private Integer id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private Page<CommentDTO> comments;
}
