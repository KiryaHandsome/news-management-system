package ru.clevertec.newssystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank
    private String text;
}
