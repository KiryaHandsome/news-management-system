package ru.clevertec.news.dto.news;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    @NotBlank(message = "News' title must be not blank")
    private String title;

    @NotBlank(message = "News' text must be not blank")
    private String text;
}
