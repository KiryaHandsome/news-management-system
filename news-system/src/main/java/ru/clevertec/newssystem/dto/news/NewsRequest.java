package ru.clevertec.newssystem.dto.news;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String text;
}
