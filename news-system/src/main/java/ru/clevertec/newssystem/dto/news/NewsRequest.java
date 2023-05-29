package ru.clevertec.newssystem.dto.news;

import lombok.Data;

@Data
public class NewsRequest {

    private String title;
    private String text;
}
